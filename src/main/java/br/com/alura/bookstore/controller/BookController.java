package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.service.AuthorService;
import br.com.alura.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public List<BookDetailsDto> list() {
        return bookService.list();
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid BookFormDto dto) {

        if (authorService.authorExists(dto.getIdAuthor())) {
            bookService.register(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Author not found! Insert a valid author!");
        }
    }
}
