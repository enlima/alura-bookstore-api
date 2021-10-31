package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.dto.BookUpdateFormDto;
import br.com.alura.bookstore.service.AuthorService;
import br.com.alura.bookstore.service.BookService;
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
@RequestMapping("/books")
@Api(tags = "Book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @PostMapping
    @ApiOperation("Register new book")
    public ResponseEntity<?> register(@RequestBody @Valid BookFormDto dto, UriComponentsBuilder uriBuilder) {

        BookDetailsDto bookDetailsDto = bookService.register(dto);

        URI uri = uriBuilder.path("/books/{id}").buildAndExpand(bookDetailsDto.getId()).toUri();

        return ResponseEntity.created(uri).body(bookDetailsDto);
    }

    @GetMapping
    @ApiOperation("List of books")
    public Page<BookDetailsDto> list(Pageable paging) {
        return bookService.list(paging);
    }

    @GetMapping("/{id}")
    @ApiOperation("Consult a specific book")
    public ResponseEntity<BookDetailsDto> detail(@PathVariable @NotNull Long id) {

        BookDetailsDto detailsDto = bookService.detail(id);

        return ResponseEntity.ok(detailsDto);
    }

    @PutMapping
    @ApiOperation("Update info about a specific book")
    public ResponseEntity<?> update(@RequestBody @Valid BookUpdateFormDto dto) {

        BookDetailsDto detailsDto = bookService.update(dto);

        return ResponseEntity.ok(detailsDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a book")
    public ResponseEntity<?> delete(@PathVariable @NotNull Long id) {

        bookService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
