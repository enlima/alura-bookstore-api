package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public AuthorDetailsDto register(AuthorFormDto dto) {

        Author author = modelMapper.map(dto, Author.class);
        author.setId(null);

        authorRepository.save(author);

        return modelMapper.map(author, AuthorDetailsDto.class);
    }

    public Page<AuthorDetailsDto> list(Pageable paging) {

        Page<Author> authors = authorRepository.findAll(paging);
        return authors.map(a -> modelMapper.map(a, AuthorDetailsDto.class));
    }

    public Author getAuthorById(Long id) {
        return authorRepository.getById(id);
    }

    public boolean authorExists(Long id) {
        return authorRepository.existsById(id);
    }
}
