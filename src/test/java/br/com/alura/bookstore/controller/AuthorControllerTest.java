package br.com.alura.bookstore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    private String authorId;

    @BeforeEach
    public void createAuthor() throws Exception {

        String jsonRegister = "{\"name\": \"Graciliano\", \"email\": \"baleia@example.com\", " +
                "\"birthdate\": \"1892-10-27\", \"miniResume\": \"A Brazilian writer.\"}";

        MvcResult result = mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRegister)).andReturn();
        String location = result.getResponse().getHeader("Location");
        this.authorId = location.substring(location.lastIndexOf("/") + 1);
    }

    @Test
    public void shouldRegisterAuthor() throws Exception {

        String json = "{\"name\": \"Tolkien\", \"email\": \"tolkien@example.com\", " +
                "\"birthdate\": \"1892-01-03\", \"miniResume\": \"An English writer.\"}";

        mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(json));
    }

    @Test
    public void shouldReturnPageableAuthorsList() throws Exception {
        mvc.perform(get("/authors").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAuthorDetails() throws Exception {

        String jsonReturn = "{\"id\": " + this.authorId + ", \"name\": \"Graciliano\", " +
                "\"email\": \"baleia@example.com\", \"birthdate\": \"1892-10-27\", " +
                "\"miniResume\": \"A Brazilian writer.\"}";

        mvc.perform(get("/authors/" + this.authorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldUpdateAuthorInfo() throws Exception {

        String json = "{\"id\": " + this.authorId + ", \"name\": \"Graciliano Ramos\", " +
                "\"email\": \"baleia@example.com\", \"birthdate\": \"1892-10-27\", " +
                "\"miniResume\": \"A Brazilian writer and journalist.\"}";

        mvc.perform(put("/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {
        mvc.perform(delete("/authors/" + this.authorId)).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBadRequestIfInvalidData() throws Exception {

        String json = "{}";

        mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfIdNotExists() throws Exception {

        String json = "{\"id\": 99999, \"name\": \"Graciliano Ramos\", " +
                "\"email\": \"baleia@example.com\", \"birthdate\": \"1892-10-27\", " +
                "\"miniResume\": \"A Brazilian writer and journalist.\"}";

        mvc.perform(put("/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
        mvc.perform(get("/authors/99999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/authors/99999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnConflictIfNameAlreadyExists() throws Exception {

        String json = "{\"name\": \"Graciliano\", \"email\": \"baleia@example.com\", " +
                "\"birthdate\": \"1892-10-27\", \"miniResume\": \"A Brazilian writer.\"}";

        mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }
}
