package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ImportOrderItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ImportOrderItemRepository importOrderItemRepository;

    public List<Book> getBooksByBookstore(Bookstore bookstore) {
        return bookRepository.findByBookstore(bookstore);
    }

    // public List<Book> searchBooks(Bookstore bookstore, String keyword) {
    // return bookRepository.findByBookstoreAndTitleContainingIgnoreCase(bookstore,
    // keyword);
    // }
    // public List<Book> searchBooks(Bookstore bookstore, String keyword) {
    // if (keyword == null || keyword.trim().isEmpty()) {
    // return bookRepository.findByBookstore(bookstore);
    // }
    // keyword = keyword.trim().toLowerCase(); // Chuẩn hóa keyword
    // return bookRepository.searchBooks(keyword).stream()
    // .filter(book -> book.getBookstore().equals(bookstore)) // Chỉ lấy sách của
    // bookstore hiện tại
    // .collect(Collectors.toList());
    // }
    public List<Book> searchBooks(Bookstore bookstore, String keyword) {
        String normalized = keyword.trim().toLowerCase().replaceAll("\\s+", " ");
        return bookRepository.searchBooksIgnoreCase(bookstore, normalized);
    }

    public Book getById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public boolean isBookUsedInImportOrder(Long bookId) {
        return importOrderItemRepository.existsByBook_Id(bookId);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
