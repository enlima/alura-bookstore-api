package br.com.alura.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private LocalDate birthdate;
    private String miniResume;

    public void updateInfo(String name, String email, LocalDate birthdate, String miniResume) {
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.miniResume = miniResume;
    }
}
