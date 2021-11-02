package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.dto.AuthorUpdateFormDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.repository.AuthorRepository;
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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private AuthorService authorService;

    public Author createAuthorA() {
        return new Author(1L, "Tolkien", "tolkien@example.com", LocalDate.now(),
                "An English writer.");
    }

    public AuthorFormDto createAuthorFormDto() {
        return new AuthorFormDto("Tolkien", "tolkien@example.com", LocalDate.now(),
                "An English writer.");
    }

    @Test
    public void shouldRegisterNewAuthorIfValidData() {

        AuthorFormDto formDto = createAuthorFormDto();

        AuthorDetailsDto detailsDto = authorService.register(formDto);

        verify(authorRepository, times(1)).save(any(Author.class));

        assertEquals(formDto.getName(), detailsDto.getName());
        assertEquals(formDto.getEmail(), detailsDto.getEmail());
        assertEquals(formDto.getBirthdate(), detailsDto.getBirthdate());
        assertEquals(formDto.getMiniResume(), detailsDto.getMiniResume());
    }

    @Test
    public void shouldNotRegisterNewAuthorIfNameAlreadyExists() {

        AuthorFormDto formDto = createAuthorFormDto();

        when(authorRepository.existsByName(formDto.getName())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> authorService.register(formDto));
    }

    @Test
    public void shouldReturnPageableAuthorsList() {

        Author authorA = createAuthorA();
        Author authorB = new Author(2L, "Machado", "axe@example.com", LocalDate.now(),
                "A Brazilian writer.");
        List<Author> authors = new ArrayList<>();
        authors.add(authorA);
        authors.add(authorB);
        Page<Author> pagedAuthors = new PageImpl<>(authors);

        when(authorRepository.findAll(any(Pageable.class))).thenReturn(pagedAuthors);

        Pageable pageRequest = PageRequest.of(0, 2);
        Page<AuthorDetailsDto> list = authorService.list(pageRequest);

        assertEquals(authorA.getId(), list.getContent().get(0).getId());
        assertEquals(authorA.getName(), list.getContent().get(0).getName());
        assertEquals(authorA.getEmail(), list.getContent().get(0).getEmail());
        assertEquals(authorA.getBirthdate(), list.getContent().get(0).getBirthdate());
        assertEquals(authorA.getMiniResume(), list.getContent().get(0).getMiniResume());
        assertEquals(authorB.getId(), list.getContent().get(1).getId());
        assertEquals(authorB.getName(), list.getContent().get(1).getName());
        assertEquals(authorB.getEmail(), list.getContent().get(1).getEmail());
        assertEquals(authorB.getBirthdate(), list.getContent().get(1).getBirthdate());
        assertEquals(authorB.getMiniResume(), list.getContent().get(1).getMiniResume());
    }

    @Test
    public void shouldReturnAuthorDetails() {

        Author authorA = createAuthorA();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorA));

        AuthorDetailsDto detailsDto = authorService.detail(1L);

        assertEquals(authorA.getId(), detailsDto.getId());
        assertEquals(authorA.getName(), detailsDto.getName());
        assertEquals(authorA.getEmail(), detailsDto.getEmail());
        assertEquals(authorA.getBirthdate(), detailsDto.getBirthdate());
        assertEquals(authorA.getMiniResume(), detailsDto.getMiniResume());
    }

    @Test
    public void shouldNotReturnAuthorDetailsIfIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> authorService.detail(1L));
    }

    @Test
    public void shouldUpdateAuthorInfoIfValidData() {

        Author authorA = createAuthorA();
        AuthorUpdateFormDto formDto = new AuthorUpdateFormDto(1L);
        formDto.setName("J.R.R. Tolkien");
        formDto.setEmail("tolkien_jrr@email.com");
        formDto.setBirthdate(LocalDate.now());
        formDto.setMiniResume("An English writer and poet.");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorA));

        AuthorDetailsDto detailsDto = authorService.update(formDto);

        assertEquals(formDto.getId(), detailsDto.getId());
        assertEquals(formDto.getName(), detailsDto.getName());
        assertEquals(formDto.getEmail(), detailsDto.getEmail());
        assertEquals(formDto.getBirthdate(), detailsDto.getBirthdate());
        assertEquals(formDto.getMiniResume(), detailsDto.getMiniResume());
    }

    @Test
    public void shouldNotUpdateAuthorInfoIfInformedNameAlreadyExists() {

        Author authorA = createAuthorA();
        AuthorUpdateFormDto formDto = new AuthorUpdateFormDto(1L);
        formDto.setName("Machado");
        formDto.setEmail("tolkien@example.com");
        formDto.setBirthdate(LocalDate.now());
        formDto.setMiniResume("An English writer.");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorA));
        when(authorRepository.existsByName(formDto.getName())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> authorService.update(formDto));
    }

    @Test
    public void shouldDeleteAuthor() {

        when(authorRepository.findById(1L)).thenReturn(Optional.of(createAuthorA()));
        when(bookService.existsBookByAuthor(any(Author.class))).thenReturn(false);

        authorService.delete(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }
}
