package br.com.alura.bookstore.repository;

import br.com.alura.bookstore.dto.ItemBookstoreDto;
import br.com.alura.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new br.com.alura.bookstore.dto.ItemBookstoreDto (a.name, COUNT(b.id), "
            + "ROUND((COUNT(b.id) * 100.0) / (SELECT COUNT(b2.id) FROM Book b2), 2)) "
            + "FROM Book b JOIN b.author a GROUP BY b.author")
    List<ItemBookstoreDto> reportBooksByAuthors();
}
