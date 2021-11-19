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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReportControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    public void generateToken() {

        User logged = new User("Gimli", "lockbearer", "2879");
        logged.addProfile(profileService.getProfile(1L));
        userRepository.save(logged);

        Authentication authentication = new UsernamePasswordAuthenticationToken(logged, logged.getLogin());
        this.token = tokenService.generateToken(authentication);
    }

    @Test
    public void shouldReturnReportList() throws Exception {
        mvc.perform(get("/reports/bookstore").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
