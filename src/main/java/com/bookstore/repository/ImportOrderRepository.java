package com.bookstore.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.bookstore.entity.ImportOrder;

// ImportOrderRepository.java
public interface ImportOrderRepository extends JpaRepositoryImplementation<ImportOrder, Long> {
}



