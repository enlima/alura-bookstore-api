package br.com.alura.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate publicationDate;
    private Integer pages;

    @ManyToOne
    private Author author;

    public void updateInfo(String title, LocalDate publicationDate, Integer pages, Author author) {
        this.title = title;
        this.publicationDate = publicationDate;
        this.pages = pages;
        this.author = author;
    }
}
