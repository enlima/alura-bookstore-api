package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.BookDetailsDto;
import br.com.alura.bookstore.dto.BookFormDto;
import br.com.alura.bookstore.dto.BookUpdateFormDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.model.Book;
import br.com.alura.bookstore.repository.AuthorRepository;
import br.com.alura.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    public Author createAuthor() {
        return new Author(1L, "Austen", "austen@example.com", LocalDate.now(),
                "An English novelist.");
    }

    public Book createBookA() {
        return new Book(1L, "Pride and Prejudice", LocalDate.now(), 408, createAuthor());
    }

    public BookFormDto createBookFormDto() {
        return new BookFormDto("Pride and Prejudice", LocalDate.now(), 408, 1L);
    }

    @Test
    public void shouldRegisterNewBookIfValidData() {

        BookFormDto formDto = createBookFormDto();

        Author author = createAuthor();
        when(authorService.getAuthorById(1L)).thenReturn(author);
        when(bookRepository.existsByTitle(formDto.getTitle())).thenReturn(false);

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
    public void shouldReturnPageableBooksList() {

        Book bookA = createBookA();
        Book bookB = new Book(1L, "Persuasion", LocalDate.now(), 268, createAuthor());
        List<Book> books = new ArrayList<>();
        books.add(bookA);
        books.add(bookB);
        Page<Book> pagedBooks = new PageImpl<>(books);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(pagedBooks);

        Pageable pageRequest = PageRequest.of(0, 2);
        Page<BookDetailsDto> list = bookService.list(pageRequest);

        assertEquals(bookA.getId(), list.getContent().get(0).getId());
        assertEquals(bookA.getTitle(), list.getContent().get(0).getTitle());
        assertEquals(bookA.getPublicationDate(), list.getContent().get(0).getPublicationDate());
        assertEquals(bookA.getPages(), list.getContent().get(0).getPages());
        assertEquals(bookA.getAuthor().getId(), list.getContent().get(0).getAuthor().getId());
        assertEquals(bookA.getAuthor().getName(), list.getContent().get(0).getAuthor().getName());
        assertEquals(bookB.getId(), list.getContent().get(1).getId());
        assertEquals(bookB.getTitle(), list.getContent().get(1).getTitle());
        assertEquals(bookB.getPublicationDate(), list.getContent().get(1).getPublicationDate());
        assertEquals(bookB.getPages(), list.getContent().get(1).getPages());
        assertEquals(bookB.getAuthor().getId(), list.getContent().get(1).getAuthor().getId());
        assertEquals(bookB.getAuthor().getName(), list.getContent().get(1).getAuthor().getName());
    }

    @Test
    public void shouldReturnBookDetails() {

        Book bookA = createBookA();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookA));

        BookDetailsDto detailsDto = bookService.detail(1L);

        assertEquals(bookA.getId(), detailsDto.getId());
        assertEquals(bookA.getTitle(), detailsDto.getTitle());
        assertEquals(bookA.getPublicationDate(), detailsDto.getPublicationDate());
        assertEquals(bookA.getPages(), detailsDto.getPages());
        assertEquals(bookA.getAuthor().getId(), detailsDto.getAuthor().getId());
        assertEquals(bookA.getAuthor().getName(), detailsDto.getAuthor().getName());
    }

    @Test
    public void shouldNotReturnBookDetailsIfIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> bookService.detail(1L));
    }

    @Test
    public void shouldUpdateBookInfoIfValidData() {

        Book bookA = createBookA();
        BookUpdateFormDto formDto = new BookUpdateFormDto(1L);
        formDto.setTitle("Prejudice and Pride");
        formDto.setPublicationDate(LocalDate.now());
        formDto.setPages(804);
        formDto.setAuthorId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookA));
        when(authorService.getAuthorById(1L)).thenReturn(createAuthor());

        BookDetailsDto detailsDto = bookService.update(formDto);

        assertEquals(formDto.getId(), detailsDto.getId());
        assertEquals(formDto.getTitle(), detailsDto.getTitle());
        assertEquals(formDto.getPublicationDate(), detailsDto.getPublicationDate());
        assertEquals(formDto.getPages(), detailsDto.getPages());
        assertEquals(formDto.getAuthorId(), detailsDto.getAuthor().getId());
    }

    @Test
    public void shouldNotUpdateBookInfoIfInformedTitleAlreadyExists() {

        Book bookA = createBookA();
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
}
