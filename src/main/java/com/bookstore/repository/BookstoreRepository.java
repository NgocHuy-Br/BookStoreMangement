package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.Bookstore;

public interface BookstoreRepository extends JpaRepository<Bookstore, Long> {

}