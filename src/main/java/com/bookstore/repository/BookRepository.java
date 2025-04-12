package com.bookstore.repository;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookstore(Bookstore bookstore);

    List<Book> findByBookstoreAndTitleContainingIgnoreCase(Bookstore bookstore, String title);

    boolean existsByCategoryId(Long categoryId);

}
