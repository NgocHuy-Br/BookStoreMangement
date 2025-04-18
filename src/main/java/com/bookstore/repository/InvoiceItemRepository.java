package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.entity.Book;
import com.bookstore.entity.Invoice;
import com.bookstore.entity.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    List<InvoiceItem> findByInvoice(Invoice invoice);

    @Query("SELECT SUM(i.quantity) FROM InvoiceItem i WHERE i.book = :book")
    Integer sumSoldQuantityByBook(@Param("book") Book book);

}
