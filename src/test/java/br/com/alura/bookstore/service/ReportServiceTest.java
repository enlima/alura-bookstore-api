package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.AuthorStatsDto;
import br.com.alura.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    public void shouldReturnReportList() {

        AuthorStatsDto mockA = new AuthorStatsDto("Machado", 60L, 60.00);
        AuthorStatsDto mockB = new AuthorStatsDto("Graciliano", 40L, 40.00);
        List<AuthorStatsDto> mockReport = new ArrayList<>();
        mockReport.add(mockA);
        mockReport.add(mockB);

        when(bookRepository.reportBooksByAuthors()).thenReturn(mockReport);

        List<AuthorStatsDto> report = reportService.reportBooksByAuthors();

        assertEquals(mockA.getAuthor(), report.get(0).getAuthor());
        assertEquals(mockA.getTotalBooks(), report.get(0).getTotalBooks());
        assertEquals(mockA.getPercentage(), report.get(0).getPercentage());
        assertEquals(mockB.getAuthor(), report.get(1).getAuthor());
        assertEquals(mockB.getTotalBooks(), report.get(1).getTotalBooks());
        assertEquals(mockB.getPercentage(), report.get(1).getPercentage());
    }
}
