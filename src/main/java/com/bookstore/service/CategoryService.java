package com.bookstore.service;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Category;
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
}
