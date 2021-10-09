package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorStatsDto {

    private String author;
    private Long totalBooks;
    private Double percentage;
}
