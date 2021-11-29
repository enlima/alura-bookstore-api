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
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private String userId;
    private String token;

    @BeforeEach
    public void generateTokenAndUser() throws Exception {

        User logged = new User("Gimli", "lockbearer", "2879", "dwarf@mail.com");
        logged.addProfile(profileService.getProfile(1L));
        userRepository.save(logged);

        Authentication authentication = new UsernamePasswordAuthenticationToken(logged, logged.getLogin());
        this.token = tokenService.generateToken(authentication);

        createUser();
    }

    public void createUser() throws Exception {

        String jsonUser = "{\"name\": \"Gandalf\", \"login\": \"mithrandir\", \"email\": \"grey@mail.com\", \"profileId\": 1}";

        MvcResult result = mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser).header("Authorization", token)).andReturn();
        String location = result.getResponse().getHeader("Location");
        assert location != null;
        this.userId = location.substring(location.lastIndexOf("/") + 1);
    }

    @Test
    public void shouldRegisterUser() throws Exception {

        String jsonUser = "{\"name\": \"Legolas\", \"login\": \"greenleaf\", \"email\": \"elf@mail.com\", \"profileId\": 2}";
        String jsonReturn = "{\"name\":\"Legolas\",\"login\":\"greenleaf\", \"email\": \"elf@mail.com\"}";

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonUser)
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldReturnPageableUsersList() throws Exception {
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUserDetails() throws Exception {

        String jsonReturn = "{\"id\": " + this.userId + ", \"name\": \"Gandalf\", \"login\": \"mithrandir\", " +
                "\"email\": \"grey@mail.com\", \"profiles\": [{\"id\": 1, \"name\": \"ROLE_ADMIN\", \"authority\": \"ROLE_ADMIN\"}]}";

        mvc.perform(get("/users/" + this.userId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldUpdateUserInfo() throws Exception {

        String jsonUpdate = "{\"id\": " + this.userId + ", \"name\": \"Gandalf the Grey\", \"login\": \"olorin\", " +
                "\"email\": \"white@mail.com\", \"profilesId\": [1, 2]}";

        String jsonReturn = "{\"id\": " + this.userId + ", \"name\": \"Gandalf the Grey\", \"login\": \"olorin\", " +
                "\"email\": \"white@mail.com\", \"profiles\": [{\"id\": 1, \"name\": \"ROLE_ADMIN\", \"authority\": \"ROLE_ADMIN\"}, " +
                "{\"id\": 2, \"name\": \"ROLE_COMMON\", \"authority\": \"ROLE_COMMON\"}]}";

        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(jsonUpdate)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonReturn));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/" + this.userId).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBadRequestIfInvalidData() throws Exception {

        String json = "{}";

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfIdNotExists() throws Exception {

        String json = "{\"id\": 99999, \"name\": \"Gandalf\", \"login\": \"mithrandir\", \"email\": \"grey@mail.com\", " +
                "\"profilesId\": [1, 2]}";

        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(get("/users/99999").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/users/99999").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnConflictIfLoginAlreadyExists() throws Exception {

        String json = "{\"name\": \"Gandalf\", \"login\": \"mithrandir\", \"email\": \"grey@mail.com\", \"profileId\": 1}";

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(status().isConflict());
    }
}
