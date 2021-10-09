package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.AuthorStatsDto;
import br.com.alura.bookstore.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportServiceService;

    @GetMapping("/bookstore")
    public List<AuthorStatsDto> reportInvestmentPortfolio() {
        return reportServiceService.reportBooksByAuthors();
    }
}
