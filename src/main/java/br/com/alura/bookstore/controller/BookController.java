package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.service.AuthorService;
import br.com.alura.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid BookFormDto dto, UriComponentsBuilder uriBuilder) {

        if (authorService.authorExists(dto.getAuthorId())) {

            BookDetailsDto bookDetailsDto = bookService.register(dto);

            URI uri = uriBuilder.path("/books/{id}").buildAndExpand(bookDetailsDto.getId()).toUri();

            return ResponseEntity.created(uri).body(bookDetailsDto);
        } else {
            return ResponseEntity.badRequest().body("Author not found! Check for a valid author_id!");
        }
    }

    @GetMapping
    public Page<BookDetailsDto> list(Pageable paging) {
        return bookService.list(paging);
    }
}
