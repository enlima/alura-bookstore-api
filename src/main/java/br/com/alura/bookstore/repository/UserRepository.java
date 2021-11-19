package br.com.alura.bookstore.repository;

import br.com.alura.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM User u JOIN FETCH u.profiles WHERE u.id = :id")
    Optional<User> findByIdWithProfiles(Long id);
}
