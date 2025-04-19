package com.bookstore.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.bookstore.entity.ImportOrder;
import com.bookstore.entity.User;

public interface ImportOrderRepository extends JpaRepositoryImplementation<ImportOrder, Long> {
    boolean existsByCreatedBy(User user);
}
