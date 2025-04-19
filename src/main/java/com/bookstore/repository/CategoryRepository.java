package com.bookstore.repository;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByBookstore(Bookstore bookstore);

    boolean existsByNameAndBookstore(String name, Bookstore bookstore);

    Category findByNameAndBookstore(String name, Bookstore bookstore);

}
