package com.bookstore.repository;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
        List<Book> findByBookstore(Bookstore bookstore);

        List<Book> findByBookstoreAndTitleContainingIgnoreCase(Bookstore bookstore, String title);

        boolean existsByCategoryId(Long categoryId);

        @Query("SELECT b FROM Book b " +
                        "WHERE b.bookstore = :bookstore AND (" +
                        "LOWER(TRIM(b.title)) LIKE %:keyword% OR " +
                        "LOWER(TRIM(b.author)) LIKE %:keyword% OR " +
                        "LOWER(TRIM(b.category.name)) LIKE %:keyword%)")
        List<Book> searchBooksIgnoreCase(@Param("bookstore") Bookstore bookstore,
                        @Param("keyword") String keyword);

        @Query("SELECT b FROM Book b WHERE b.bookstore = :bookstore ORDER BY " +
                        "(SELECT SUM(i.quantity) FROM InvoiceItem i WHERE i.book = b) DESC")
        List<Book> findTopSellingBooks(@Param("bookstore") Bookstore bookstore, Pageable pageable);

}
