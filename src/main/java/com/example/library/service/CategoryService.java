package com.example.library.service;

import com.example.library.model.Category;
import com.example.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public Category createCategory(Category category) {
        if (category == null) {
            throw new RuntimeException("Category cannot be 'null'");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public Category readCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Not found");
        });
    }
}
