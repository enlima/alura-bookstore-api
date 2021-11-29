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
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private String authorId;
    private String bookId;
    private String token;

    @BeforeEach
    public void generateTokenAndBook() throws Exception {

        User logged = new User("Gimli", "lockbearer", "2879", "dwarf@mail.com");
        logged.addProfile(profileService.getProfile(1L));
        userRepository.save(logged);

        Authentication authentication = new UsernamePasswordAuthenticationToken(logged, logged.getLogin());
        this.token = tokenService.generateToken(authentication);

        createBook();
    }

    public void createBook() throws Exception {

        String jsonAuthor = "{\"name\": \"Graciliano\", \"email\": \"baleia@example.com\", " +
                "\"birthdate\": \"1892-10-27\", \"miniResume\": \"A Brazilian writer.\"}";

        MvcResult resultAuthor = mvc.perform(post("/authors").contentType(MediaType.APPLICATION_JSON)
                .content(jsonAuthor).header("Authorization", token)).andReturn();
        String locationAuthor = resultAuthor.getResponse().getHeader("Location");
        assert locationAuthor != null;
        this.authorId = locationAuthor.substring(locationAuthor.lastIndexOf("/") + 1);

        String jsonBook = "{\"title\": \"Vidas Secas\", \"publicationDate\": \"1938-01-01\", \"pages\": 158, " +
                "\"authorId\": " + this.authorId + "}";

        MvcResult resultBook = mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook).header("Authorization", token)).andReturn();
        String locationBook = resultBook.getResponse().getHeader("Location");
        assert locationBook != null;
        this.bookId = locationBook.substring(locationBook.lastIndexOf("/") + 1);
    }

    @Test
    public void shouldRegisterBook() throws Exception {

        String jsonRegister = "{\"title\": \"Memórias do Cárcere\", \"publicationDate\": \"1953-01-01\", \"pages\": 686, " +
                "\"authorId\": " + this.authorId + "}";

        String jsonReturn = "{\"title\": \"Memórias do Cárcere\", " +
                "\"publicationDate\": \"1953-01-01\", \"pages\": 686, " +
                "\"author\": {\"id\": " + this.authorId + ",\"name\": \"Graciliano\"}}";

        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(jsonRegister)
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldReturnPageableBooksList() throws Exception {
        mvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBookDetails() throws Exception {

        String jsonReturn = "{\"title\": \"Vidas Secas\", \"publicationDate\": \"1938-01-01\", \"pages\": 158, " +
                "\"author\": {\"id\": " + this.authorId + ",\"name\": \"Graciliano\"}}";

        mvc.perform(get("/books/" + this.bookId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldUpdateBookInfo() throws Exception {

        String jsonUpdate = "{\"id\": " + this.bookId + ", \"title\": \"Secas Vidas\", " +
                "\"publicationDate\": \"1938-12-20\", \"pages\": 408, \"authorId\": " + this.authorId + "}";

        String jsonReturn = "{\"id\": " + this.bookId + ", \"title\": \"Secas Vidas\", " +
                "\"publicationDate\": \"1938-12-20\", \"pages\": 408, " +
                "\"author\": {\"id\": " + this.authorId + ",\"name\": \"Graciliano\"}}";

        mvc.perform(put("/books").contentType(MediaType.APPLICATION_JSON).content(jsonUpdate)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/books/" + this.bookId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBadRequestIfInvalidData() throws Exception {

        String json = "{}";

        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/books").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfIdNotExists() throws Exception {

        String jsonInvalidBookId = "{\"id\": 99999, \"title\": \"Secas Vidas\", " +
                "\"publicationDate\": \"1938-12-20\", \"pages\": 408, \"authorId\": " + this.authorId + "}";
        String jsonInvalidAuthorId = "{\"id\": " + this.bookId + ", \"title\": \"Secas Vidas\", " +
                "\"publicationDate\": \"1938-12-20\", \"pages\": 408, \"authorId\": 99999}";

        mvc.perform(put("/books").contentType(MediaType.APPLICATION_JSON).content(jsonInvalidBookId)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(put("/books").contentType(MediaType.APPLICATION_JSON).content(jsonInvalidAuthorId)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(get("/books/99999").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/books/99999").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnConflictIfTitleAlreadyExists() throws Exception {

        String json = "{\"title\": \"Vidas Secas\", \"publicationDate\": \"1938-01-01\", \"pages\": 158, " +
                "\"authorId\": " + this.authorId + "}";

        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isConflict());
    }
}
