package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailsDto {

    private Long id;
    private String title;
    private LocalDate publicationDate;
    private Integer pages;
    private AuthorBasicsDto author;
}
