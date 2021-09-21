package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorDetailsDto;
import br.com.alura.bookstore.dto.AuthorFormDto;
import br.com.alura.bookstore.model.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    //hashmap acting as a database
    private final HashMap<Integer, Author> bdAuthors = new HashMap<>();
    private final List<Author> authors = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();
    private int id = 0;

    public List<AuthorDetailsDto> list() {

        return authors.stream()
                .map(a -> modelMapper.map(a, AuthorDetailsDto.class))
                .collect(Collectors.toList());
    }

    public void register(AuthorFormDto dto) {

        Author author = modelMapper.map(dto, Author.class);

        author.setId(id += 1);

        authors.add(author);

        bdAuthors.put(author.getId(), author);
    }

    public Author findAuthorById(Integer id) {
        return bdAuthors.get(id);
    }
}
