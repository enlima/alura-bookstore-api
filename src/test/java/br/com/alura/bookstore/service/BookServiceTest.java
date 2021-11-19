package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorBasicsDto;
import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.dto.BookUpdateFormDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.model.Book;
import br.com.alura.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    public Author createAuthor() {
        return new Author(1L, "Austen", "austen@example.com", LocalDate.now(),
                "An English novelist.");
    }

    public Book createBook() {
        return new Book(1L, "Pride and Prejudice", LocalDate.now(), 408, createAuthor());
    }

    public BookFormDto createBookFormDto() {
        return new BookFormDto("Pride and Prejudice", LocalDate.now(), 408, 1L);
    }

    @Test
    public void shouldRegisterNewBookIfValidData() {

        BookFormDto formDto = createBookFormDto();
        Book book = createBook();

        Author author = createAuthor();
        when(authorService.getAuthor(1L)).thenReturn(author);
        when(bookRepository.existsByTitle(formDto.getTitle())).thenReturn(false);
        when(modelMapper.map(formDto, Book.class)).thenReturn(book);
        when(modelMapper.map(book, BookDetailsDto.class))
                .thenReturn(new BookDetailsDto(book.getId(), book.getTitle(), book.getPublicationDate(),
                        book.getPages(), new AuthorBasicsDto(book.getAuthor().getId(), book.getAuthor().getName())));

        BookDetailsDto detailsDto = bookService.register(formDto);

        verify(bookRepository, times(1)).save(any(Book.class));

        assertEquals(formDto.getTitle(), detailsDto.getTitle());
        assertEquals(formDto.getPublicationDate(), detailsDto.getPublicationDate());
        assertEquals(formDto.getPages(), detailsDto.getPages());
        assertEquals(formDto.getAuthorId(), detailsDto.getAuthor().getId());
        assertEquals(author.getName(), detailsDto.getAuthor().getName());
    }

    @Test
    public void shouldNotRegisterNewBookIfTitleAlreadyExists() {

        BookFormDto formDto = createBookFormDto();

        when(bookRepository.existsByTitle(formDto.getTitle())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> bookService.register(formDto));
    }

    @Test
    public void shouldReturnBookDetails() {

        Book book = createBook();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDetailsDto.class))
                .thenReturn(new BookDetailsDto(book.getId(), book.getTitle(), book.getPublicationDate(),
                        book.getPages(), new AuthorBasicsDto(book.getAuthor().getId(), book.getAuthor().getName())));

        BookDetailsDto detailsDto = bookService.detail(1L);

        assertEquals(book.getId(), detailsDto.getId());
        assertEquals(book.getTitle(), detailsDto.getTitle());
        assertEquals(book.getPublicationDate(), detailsDto.getPublicationDate());
        assertEquals(book.getPages(), detailsDto.getPages());
        assertEquals(book.getAuthor().getId(), detailsDto.getAuthor().getId());
        assertEquals(book.getAuthor().getName(), detailsDto.getAuthor().getName());
    }

    @Test
    public void shouldNotReturnBookDetailsIfIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> bookService.detail(1L));
    }

    @Test
    public void shouldUpdateBookInfoIfValidData() {

        Book book = createBook();
        BookUpdateFormDto formDto = new BookUpdateFormDto(1L);
        formDto.setTitle("Prejudice and Pride");
        formDto.setPublicationDate(LocalDate.now());
        formDto.setPages(804);
        formDto.setAuthorId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.getAuthor(1L)).thenReturn(createAuthor());
        when(modelMapper.map(book, BookDetailsDto.class))
                .thenReturn(new BookDetailsDto(formDto.getId(), formDto.getTitle(), formDto.getPublicationDate(),
                        formDto.getPages(), new AuthorBasicsDto(book.getAuthor().getId(), book.getAuthor().getName())));

        BookDetailsDto detailsDto = bookService.update(formDto);

        assertEquals(formDto.getId(), detailsDto.getId());
        assertEquals(formDto.getTitle(), detailsDto.getTitle());
        assertEquals(formDto.getPublicationDate(), detailsDto.getPublicationDate());
        assertEquals(formDto.getPages(), detailsDto.getPages());
        assertEquals(formDto.getAuthorId(), detailsDto.getAuthor().getId());
    }

    @Test
    public void shouldNotUpdateBookInfoIfInformedTitleAlreadyExists() {

        Book bookA = createBook();
        BookUpdateFormDto formDto = new BookUpdateFormDto(1L);
        formDto.setTitle("Prejudice and Pride");
        formDto.setPublicationDate(LocalDate.now());
        formDto.setPages(804);
        formDto.setAuthorId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookA));
        when(bookRepository.existsByTitle(formDto.getTitle())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> bookService.update(formDto));
    }

    @Test
    public void shouldDeleteBook() {
        bookService.delete(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldCheckIfExistsBookByAuthor() {
        bookService.existsBookByAuthor(createAuthor());
        verify(bookRepository, times(1)).existsByAuthor(any(Author.class));
    }
}
