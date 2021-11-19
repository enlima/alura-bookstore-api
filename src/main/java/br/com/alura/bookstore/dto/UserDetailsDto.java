package br.com.alura.bookstore.dto;

import br.com.alura.bookstore.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto extends UserDto {

    private List<Profile> profiles = new ArrayList<>();

    public UserDetailsDto(Long id, String name, String login, List<Profile> profiles) {
        super.setId(id);
        super.setName(name);
        super.setLogin(login);
        this.profiles = profiles;
    }
}
