package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorDetailsDto> register(@RequestBody @Valid AuthorFormDto dto, UriComponentsBuilder uriBuilder) {

        AuthorDetailsDto authorDetailsDto = authorService.register(dto);

        URI uri = uriBuilder.path("/authors/{id}").buildAndExpand(authorDetailsDto.getId()).toUri();

        return ResponseEntity.created(uri).body(authorDetailsDto);
    }

    @GetMapping
    public Page<AuthorDetailsDto> list(Pageable paging) {
        return authorService.list(paging);
    }
}
