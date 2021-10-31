package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Http400Dto {

    private String field;
    private String message;
}
