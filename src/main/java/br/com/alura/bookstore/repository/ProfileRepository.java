package br.com.alura.bookstore.repository;

import br.com.alura.bookstore.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
