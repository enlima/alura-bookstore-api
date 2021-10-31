package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.dto.AuthorUpdateFormDto;
import br.com.alura.bookstore.service.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/authors")
@Api(tags = "Author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    @ApiOperation("Register new author")
    public ResponseEntity<AuthorDetailsDto> register(@RequestBody @Valid AuthorFormDto dto, UriComponentsBuilder uriBuilder) {

        AuthorDetailsDto authorDetailsDto = authorService.register(dto);

        URI uri = uriBuilder.path("/authors/{id}").buildAndExpand(authorDetailsDto.getId()).toUri();

        return ResponseEntity.created(uri).body(authorDetailsDto);
    }

    @GetMapping
    @ApiOperation("List of authors")
    public Page<AuthorDetailsDto> list(Pageable paging) {
        return authorService.list(paging);
    }

    @GetMapping("/{id}")
    @ApiOperation("Consult a specific author")
    public ResponseEntity<AuthorDetailsDto> detail(@PathVariable @NotNull Long id) {

        AuthorDetailsDto detailsDto = authorService.detail(id);

        return ResponseEntity.ok(detailsDto);
    }

    @PutMapping
    @ApiOperation("Update info about a specific author")
    public ResponseEntity<AuthorDetailsDto> update(@RequestBody @Valid AuthorUpdateFormDto dto) {

        AuthorDetailsDto detailsDto = authorService.update(dto);

        return ResponseEntity.ok(detailsDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete an author")
    public ResponseEntity<?> delete(@PathVariable @NotNull Long id) {

        authorService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
