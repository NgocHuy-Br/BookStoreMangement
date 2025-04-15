package com.bookstore.repository;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByBookstore(Bookstore bookstore);

    List<Customer> findByBookstoreAndNameContainingIgnoreCase(Bookstore bookstore, String keyword);

}
