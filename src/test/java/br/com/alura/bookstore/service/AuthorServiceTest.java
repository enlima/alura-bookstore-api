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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthorService authorService;

    public Author createAuthor() {
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
        Author author = createAuthor();

        when(modelMapper.map(formDto, Author.class)).thenReturn(author);
        when(modelMapper.map(author, AuthorDetailsDto.class))
                .thenReturn(new AuthorDetailsDto(author.getId(), author.getName(), author.getEmail(),
                        author.getBirthdate(), author.getMiniResume()));

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
    public void shouldReturnAuthorDetails() {

        Author author = createAuthor();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(modelMapper.map(author, AuthorDetailsDto.class))
                .thenReturn(new AuthorDetailsDto(author.getId(), author.getName(), author.getEmail(),
                        author.getBirthdate(), author.getMiniResume()));

        AuthorDetailsDto detailsDto = authorService.detail(1L);

        assertEquals(author.getId(), detailsDto.getId());
        assertEquals(author.getName(), detailsDto.getName());
        assertEquals(author.getEmail(), detailsDto.getEmail());
        assertEquals(author.getBirthdate(), detailsDto.getBirthdate());
        assertEquals(author.getMiniResume(), detailsDto.getMiniResume());
    }

    @Test
    public void shouldNotReturnAuthorDetailsIfIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> authorService.detail(1L));
    }

    @Test
    public void shouldUpdateAuthorInfoIfValidData() {

        Author author = createAuthor();
        AuthorUpdateFormDto formDto = new AuthorUpdateFormDto(1L);
        formDto.setName("J.R.R. Tolkien");
        formDto.setEmail("tolkien_jrr@email.com");
        formDto.setBirthdate(LocalDate.now());
        formDto.setMiniResume("An English writer and poet.");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(modelMapper.map(author, AuthorDetailsDto.class))
                .thenReturn(new AuthorDetailsDto(formDto.getId(), formDto.getName(), formDto.getEmail(),
                        formDto.getBirthdate(), formDto.getMiniResume()));

        AuthorDetailsDto detailsDto = authorService.update(formDto);

        assertEquals(formDto.getId(), detailsDto.getId());
        assertEquals(formDto.getName(), detailsDto.getName());
        assertEquals(formDto.getEmail(), detailsDto.getEmail());
        assertEquals(formDto.getBirthdate(), detailsDto.getBirthdate());
        assertEquals(formDto.getMiniResume(), detailsDto.getMiniResume());
    }

    @Test
    public void shouldNotUpdateAuthorInfoIfInformedNameAlreadyExists() {

        Author authorA = createAuthor();
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

        when(authorRepository.findById(1L)).thenReturn(Optional.of(createAuthor()));
        when(bookService.existsBookByAuthor(any(Author.class))).thenReturn(false);

        authorService.delete(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }
}
