package br.com.alura.bookstore.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private int id;
    private String title;
    private LocalDate releaseDate;
    private int pages;
    private Author author;
}
