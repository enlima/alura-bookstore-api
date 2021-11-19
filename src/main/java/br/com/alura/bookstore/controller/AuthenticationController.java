package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.LoginFormDto;
import br.com.alura.bookstore.dto.TokenDto;
import br.com.alura.bookstore.infra.security.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "Authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    @ApiOperation("Authenticate login")
    public TokenDto authenticate(@RequestBody @Valid LoginFormDto dto) {
        return new TokenDto(authService.authenticate(dto));
    }
}
