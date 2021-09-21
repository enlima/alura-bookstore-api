package br.com.alura.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDetailsDto {

    private int id;
    private String title;
    private LocalDate releaseDate;
    private int pages;
    private AuthorBasicsDto author;
}
