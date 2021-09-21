package br.com.alura.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class BookFormDto {

    @NotBlank
    @Size(min = 10)
    private String title;

    @PastOrPresent
    private LocalDate releaseDate;

    @Min(100)
    private int pages;

    @Min(1)
    private int idAuthor;
}
