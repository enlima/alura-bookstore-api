package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.AuthorStatsDto;
import br.com.alura.bookstore.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
@Api(tags = "Report")
public class ReportController {

    @Autowired
    private ReportService reportServiceService;

    @GetMapping("/bookstore")
    @ApiOperation("Books by authors report")
    public List<AuthorStatsDto> reportInvestmentPortfolio() {
        return reportServiceService.reportBooksByAuthors();
    }
}
