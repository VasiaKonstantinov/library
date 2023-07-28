package com.example.library.controller;

import com.example.library.model.Category;
import com.example.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/categories/create/users/{userId}")
    public String createCategory(@PathVariable("userId") Long userId, Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "category-create";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @PostMapping("/categories/create/users/{userId}")
    public String createCategory(@PathVariable("userId") Long userId,
                                 @ModelAttribute Category category){
        categoryService.createCategory(category);
        return "redirect:/books/categories/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/all")
    public String getAllCategories(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories-list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/{categoryId}/read")
    public String readCategory(@PathVariable("categoryId") Long categoryId, Model model){
        Category category = categoryService.readCategory(categoryId);
        model.addAttribute("category", category);
        return "category-info";
    }
}
