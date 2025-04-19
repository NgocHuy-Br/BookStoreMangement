package com.bookstore.service;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Category;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CategoryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> getCategoriesByBookstore(Bookstore bookstore) {
        return categoryRepository.findByBookstore(bookstore);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Autowired
    private BookRepository bookRepository;

    public boolean isCategoryUsed(Long categoryId) {
        return bookRepository.existsByCategoryId(categoryId);
    }

    public boolean isNameExists(String name, Bookstore bookstore) {
        return categoryRepository.existsByNameAndBookstore(name, bookstore);
    }

    public boolean isNameTakenByOther(Long id, String name, Bookstore bookstore) {
        Category existing = categoryRepository.findByNameAndBookstore(name, bookstore);
        return existing != null && !existing.getId().equals(id);
    }

}
