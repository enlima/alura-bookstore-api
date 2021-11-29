package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.infra.security.TokenService;
import br.com.alura.bookstore.model.User;
import br.com.alura.bookstore.repository.UserRepository;
import br.com.alura.bookstore.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private String authorId;
    private String token;

    @BeforeEach
    public void generateTokenAndAuthor() throws Exception {

        User logged = new User("Gimli", "lockbearer", "2879", "dwarf@mail.com");
        logged.addProfile(profileService.getProfile(1L));
        userRepository.save(logged);

        Authentication authentication = new UsernamePasswordAuthenticationToken(logged, logged.getLogin());
        this.token = tokenService.generateToken(authentication);

        createAuthor();
    }

    public void createAuthor() throws Exception {

        String jsonAuthor = "{\"name\": \"Graciliano\", \"email\": \"baleia@example.com\", " +
                "\"birthdate\": \"1892-10-27\", \"miniResume\": \"A Brazilian writer.\"}";

        MvcResult result = mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON)
                .content(jsonAuthor).header("Authorization", token)).andReturn();
        String location = result.getResponse().getHeader("Location");
        assert location != null;
        this.authorId = location.substring(location.lastIndexOf("/") + 1);
    }

    @Test
    public void shouldRegisterAuthor() throws Exception {

        String json = "{\"name\": \"Tolkien\", \"email\": \"tolkien@example.com\", " +
                "\"birthdate\": \"1892-01-03\", \"miniResume\": \"An English writer.\"}";

        mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(json));
    }

    @Test
    public void shouldReturnPageableAuthorsList() throws Exception {
        mvc.perform(get("/authors").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAuthorDetails() throws Exception {

        String jsonReturn = "{\"id\": " + this.authorId + ", \"name\": \"Graciliano\", " +
                "\"email\": \"baleia@example.com\", \"birthdate\": \"1892-10-27\", " +
                "\"miniResume\": \"A Brazilian writer.\"}";

        mvc.perform(get("/authors/" + this.authorId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldUpdateAuthorInfo() throws Exception {

        String json = "{\"id\": " + this.authorId + ", \"name\": \"Graciliano Ramos\", " +
                "\"email\": \"baleia@example.com\", \"birthdate\": \"1892-10-27\", " +
                "\"miniResume\": \"A Brazilian writer and journalist.\"}";

        mvc.perform(put("/authors").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {
        mvc.perform(delete("/authors/" + this.authorId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBadRequestIfInvalidData() throws Exception {

        String json = "{}";

        mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/authors").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfIdNotExists() throws Exception {

        String json = "{\"id\": 99999, \"name\": \"Graciliano Ramos\", " +
                "\"email\": \"baleia@example.com\", \"birthdate\": \"1892-10-27\", " +
                "\"miniResume\": \"A Brazilian writer and journalist.\"}";

        mvc.perform(put("/authors").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(get("/authors/99999").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/authors/99999").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnConflictIfNameAlreadyExists() throws Exception {

        String json = "{\"name\": \"Graciliano\", \"email\": \"baleia@example.com\", " +
                "\"birthdate\": \"1892-10-27\", \"miniResume\": \"A Brazilian writer.\"}";

        mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldNotDeleteIfExistsBookByAuthor() throws Exception {

        String jsonBook = "{\"title\": \"Vidas Secas\", \"publicationDate\": \"1938-01-01\", \"pages\": 158, " +
                "\"authorId\": " + this.authorId + "}";

        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook).header("Authorization", token));

        mvc.perform(delete("/authors/" + this.authorId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isConflict());
    }
}
