package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateFormDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String login;

    @NotNull
    private List<Long> profilesId;
}
