package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Http409Dto {

    private LocalDate date;
    private String className;
    private String exceptionMessage;
    private String uri;
}
