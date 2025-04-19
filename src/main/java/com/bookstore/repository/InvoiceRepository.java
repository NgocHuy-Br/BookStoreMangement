package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Invoice;
import com.bookstore.entity.User;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByBookstore(Bookstore bookstore);

    boolean existsByUser(User user);

}
