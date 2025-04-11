package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BookService {
    void save(Book book);

    void delete(Long id);

    Book findById(Long id);

    List<Book> findByBookstore(Bookstore bookstore);

    List<Book> searchByTitleAndBookstore(String keyword, Bookstore bookstore);
}
