package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDto {

    @NotBlank
    private String name;

    @NotBlank
    private String login;

    @NotNull
    @Min(1)
    private Long profileId;
}
