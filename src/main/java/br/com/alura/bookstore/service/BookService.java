package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.model.Book;
import br.com.alura.bookstore.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public BookDetailsDto register(BookFormDto dto) {

        Book book = modelMapper.map(dto, Book.class);
        book.setId(null);
        book.setAuthor(authorService.getAuthorById(dto.getAuthorId()));

        bookRepository.save(book);

        return modelMapper.map(book, BookDetailsDto.class);
    }

    public Page<BookDetailsDto> list(Pageable paging) {

        Page<Book> books = bookRepository.findAll(paging);
        return books.map(b -> modelMapper.map(b, BookDetailsDto.class));
    }
}
