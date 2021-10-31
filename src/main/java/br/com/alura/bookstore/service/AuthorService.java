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

    private final ModelMapper modelMapper = new ModelMapper();

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

        Author author = getAuthorById(id);
        return modelMapper.map(author, AuthorDetailsDto.class);
    }

    @Transactional
    public AuthorDetailsDto update(AuthorUpdateFormDto dto) {

        Author author = authorRepository.getById(dto.getId());

        if (!author.getName().trim().equals(dto.getName().trim())) {
            checkIfAuthorAlreadyExists(dto.getName());
        }

        author.updateInfo(dto.getName(), dto.getEmail(), dto.getBirthdate(), dto.getMiniResume());

        return modelMapper.map(author, AuthorDetailsDto.class);
    }

    public void checkIfAuthorAlreadyExists(String name) {

        if (authorRepository.existsByName(name.trim())) {
            throw new DataIntegrityViolationException("The author '" + name.trim() + "' already exists " +
                    "associated with a different author ID!");
        }
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Informed author " +
                "(ID: " + id + ") not found!"));
    }

    @Transactional
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
