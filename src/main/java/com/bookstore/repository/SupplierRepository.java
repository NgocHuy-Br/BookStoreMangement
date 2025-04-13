package com.bookstore.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.bookstore.entity.Supplier;

// SupplierRepository.java
public interface SupplierRepository extends JpaRepositoryImplementation<Supplier, Long> {
}
