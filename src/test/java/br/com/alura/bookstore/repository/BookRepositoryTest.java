package br.com.alura.bookstore.repository;

import br.com.alura.bookstore.dto.AuthorStatsDto;
import br.com.alura.bookstore.model.Author;
import br.com.alura.bookstore.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEm;

    @Test
    public void checkIfBookExistsByTitleTest() {

        Author author = new Author(null, "Tolkien", "tolkien@example.com", LocalDate.now(),
                "An English writer.");
        testEm.persist(author);

        Book book = new Book(null, "The Hobbit", LocalDate.now(), 310, author);
        testEm.persist(book);

        assertTrue(bookRepository.existsByTitle("The Hobbit"));
        assertFalse(bookRepository.existsByTitle("Vidas Secas"));
    }

    @Test
    public void reportBooksByAuthorsTest() {

        Author authorA = new Author(null, "Tolkien", "tolkien@example.com", LocalDate.now(),
                "An English writer.");
        Author authorB = new Author(null, "Machado", "axe@example.com", LocalDate.now(),
                "An Brazilian writer.");
        Author authorC = new Author(null, "Austen", "austen@example.com", LocalDate.now(),
                "An English novelist.");
        Author authorD = new Author(null, "Graciliano", "ramos@example.com", LocalDate.now(),
                "An Brazilian writer.");

        testEm.persist(authorA);
        testEm.persist(authorB);
        testEm.persist(authorC);
        testEm.persist(authorD);

        Book book1 = new Book(null, "The Hobbit", LocalDate.now(), 310, authorA);
        Book book2 = new Book(null, "The Silmarillion", LocalDate.now(), 481, authorA);
        Book book3 = new Book(null, "O Alienista", LocalDate.now(), 104, authorB);
        Book book4 = new Book(null, "Memórias Póstumas de Brás Cubas", LocalDate.now(), 367, authorB);
        Book book5 = new Book(null, "Dom Casmurro", LocalDate.now(), 287, authorB);
        Book book6 = new Book(null, "Quincas Borba", LocalDate.now(), 294, authorB);
        Book book7 = new Book(null, "Esaú e Jacó", LocalDate.now(), 224, authorB);
        Book book8 = new Book(null, "Pride and Prejudice", LocalDate.now(), 408, authorC);
        Book book9 = new Book(null, "Sense and Sensibility", LocalDate.now(), 222, authorC);
        Book book10 = new Book(null, "Mansfield Park", LocalDate.now(), 560, authorC);
        Book book11 = new Book(null, "Vidas Secas", LocalDate.now(), 158, authorD);

        testEm.persist(book1);
        testEm.persist(book2);
        testEm.persist(book3);
        testEm.persist(book4);
        testEm.persist(book5);
        testEm.persist(book6);
        testEm.persist(book7);
        testEm.persist(book8);
        testEm.persist(book9);
        testEm.persist(book10);
        testEm.persist(book11);

        List<AuthorStatsDto> report = bookRepository.reportBooksByAuthors();

        Assertions.assertThat(report).hasSize(4)
                .extracting(AuthorStatsDto::getAuthor, AuthorStatsDto::getTotalBooks, AuthorStatsDto::getPercentage)
                .containsExactlyInAnyOrder(Assertions.tuple("Machado", 5L, 45.45),
                        Assertions.tuple("Austen", 3L, 27.27),
                        Assertions.tuple("Tolkien", 2L, 18.18),
                        Assertions.tuple("Graciliano", 1L, 9.09));
    }
}
