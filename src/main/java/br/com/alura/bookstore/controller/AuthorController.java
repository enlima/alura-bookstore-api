package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public List<AuthorDetailsDto> list() {
        return authorService.list();
    }

    @PostMapping
    public void register(@RequestBody @Valid AuthorFormDto dto) {
        authorService.register(dto);
    }
}
