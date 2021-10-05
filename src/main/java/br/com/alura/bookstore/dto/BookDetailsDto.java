package br.com.alura.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDetailsDto {

    private Long id;
    private String title;
    private LocalDate publicationDate;
    private Integer pages;
    private AuthorBasicsDto author;
}
