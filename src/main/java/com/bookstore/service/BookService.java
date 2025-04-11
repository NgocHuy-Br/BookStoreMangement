package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getBooksByBookstore(Bookstore bookstore) {
        return bookRepository.findByBookstore(bookstore);
    }

    public List<Book> searchBooks(Bookstore bookstore, String keyword) {
        return bookRepository.findByBookstoreAndTitleContainingIgnoreCase(bookstore, keyword);
    }

    public Book getById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
