package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ImportOrderItemRepository;
import com.bookstore.repository.InvoiceItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ImportOrderItemRepository importOrderItemRepository;

    // public List<Book> getBooksByBookstore(Bookstore bookstore) {
    // return bookRepository.findByBookstore(bookstore);
    // }
    public List<Book> getBooksByBookstore(Bookstore bookstore) {
        List<Book> books = bookRepository.findByBookstore(bookstore);
        for (Book book : books) {
            int sold = getSoldQuantity(book);
            book.setSoldQuantity(sold);
            // Không cần gán lại inventory vì JPA đã map, trừ khi bạn override Book
            // constructor
        }
        return books;
    }

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

    public List<Book> findTopSellingBooks(Bookstore bookstore, int top) {
        Pageable limit = PageRequest.of(0, top);
        return bookRepository.findTopSellingBooks(bookstore, limit);
    }

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    public int getSoldQuantity(Book book) {
        Integer quantity = invoiceItemRepository.sumSoldQuantityByBook(book);
        return quantity != null ? quantity : 0;
    }

}
