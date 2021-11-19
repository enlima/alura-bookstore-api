package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.dto.AuthorUpdateFormDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public AuthorDetailsDto register(AuthorFormDto dto) {

        checkIfAuthorAlreadyExists(dto.getName());

        Author author = modelMapper.map(dto, Author.class);
        author.setId(null);

        authorRepository.save(author);

        return modelMapper.map(author, AuthorDetailsDto.class);
    }

    public Page<AuthorDetailsDto> list(Pageable paging) {

        Page<Author> authors = authorRepository.findAll(paging);
        return authors.map(a -> modelMapper.map(a, AuthorDetailsDto.class));
    }

    public AuthorDetailsDto detail(Long id) {

        Author author = getAuthor(id);
        return modelMapper.map(author, AuthorDetailsDto.class);
    }

    @Transactional
    public AuthorDetailsDto update(AuthorUpdateFormDto dto) {

        Author author = getAuthor(dto.getId());

        if (!author.getName().trim().equals(dto.getName().trim())) {
            checkIfAuthorAlreadyExists(dto.getName());
        }

        author.updateInfo(dto.getName(), dto.getEmail(), dto.getBirthdate(), dto.getMiniResume());

        return modelMapper.map(author, AuthorDetailsDto.class);
    }

    public void checkIfAuthorAlreadyExists(String name) {

        if (authorRepository.existsByName(name.trim())) {
            throw new DataIntegrityViolationException("Author '" + name.trim() + "' already exists " +
                    "associated with a different Author ID!");
        }
    }

    public Author getAuthor(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Informed author " +
                "(ID: " + id + ") not found!"));
    }

    @Transactional
    public void delete(Long id) {

        if (bookService.existsBookByAuthor(getAuthor(id))) {
            throw new DataIntegrityViolationException("Author (ID: " + id + ") cannot be deleted because it's " +
                    "currently associated with one or more books.");
        }

        authorRepository.deleteById(id);
    }
}
