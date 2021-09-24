package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private AuthorService authorService;

    private final List<Book> books = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();
    private int id = 0;

    public List<BookDetailsDto> list() {

        return books.stream()
                .map(b -> modelMapper.map(b, BookDetailsDto.class))
                .collect(Collectors.toList());
    }

    public void register(BookFormDto dto) {

        Book book = modelMapper.map(dto, Book.class);
        book.setId(id += 1);
        book.setAuthor(authorService.findAuthorById(dto.getIdAuthor()));

        books.add(book);
    }
}
