package com.bookstore.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.bookstore.entity.ImportOrderItem;

// ImportOrderItemRepository.java
public interface ImportOrderItemRepository extends JpaRepositoryImplementation<ImportOrderItem, Long> {
}
