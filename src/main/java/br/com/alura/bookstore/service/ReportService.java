package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorStatsDto;
import br.com.alura.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private BookRepository bookRepository;

    public List<AuthorStatsDto> reportBooksByAuthors() {
        return bookRepository.reportBooksByAuthors();
    }
}
