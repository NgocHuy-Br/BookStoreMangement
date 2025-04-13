package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Supplier;

// SupplierRepository.java
public interface SupplierRepository extends JpaRepositoryImplementation<Supplier, Long> {
    boolean existsByNameIgnoreCaseAndBookstore(String name, Bookstore bookstore);

    List<Supplier> findByBookstore(Bookstore bookstore);

}
