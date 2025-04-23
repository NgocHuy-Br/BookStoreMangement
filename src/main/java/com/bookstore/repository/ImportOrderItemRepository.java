package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.bookstore.entity.ImportOrder;
import com.bookstore.entity.ImportOrderItem;

public interface ImportOrderItemRepository extends JpaRepositoryImplementation<ImportOrderItem, Long> {

    List<ImportOrderItem> findByImportOrder(ImportOrder importOrder);

    boolean existsByBook_Id(Long bookId);

}
