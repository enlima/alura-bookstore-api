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
public class AuthorDetailsDto {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthdate;
    private String miniResume;
}
