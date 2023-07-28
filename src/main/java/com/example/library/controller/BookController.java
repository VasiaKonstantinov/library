package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Category;
import com.example.library.service.BookService;
import com.example.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/book-create/users/{userId}")
    public String showBookCreateForm(Model model, @PathVariable("userId") Long userId) {
        Book book = new Book();
        model.addAttribute("book", book);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "book-create";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @PostMapping("/create/users/{userId}")
    public String createBook(@PathVariable("userId") Long userId,
                             @ModelAttribute @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "book-create";
        }

        bookService.create(book);
        return "redirect:/books/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{bookId}/read")
    public String bookInfo(@PathVariable("bookId") Long bookId, Model model){
        Book book = bookService.findById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("categoryName", book.getCategory().getName());
        return "book-info";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/{bookId}/update/users/{userId}")
    public String updateBook(@PathVariable("userId") Long userId,
                             @PathVariable("bookId") Long bookId, Model model){
        Book book = bookService.findById(bookId);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        return "book-update";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @PostMapping("/{bookId}/update/users/{userId}")
    public String updateBook(@PathVariable("userId") Long userId,
                             @PathVariable("bookId") Long bookId,
                             @ModelAttribute("book") Book book){
        Book oldBook = bookService.findById(bookId);
        book.setId(oldBook.getId());
        bookService.update(book);
        return "redirect:/books/" + book.getId() + "/read";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') and authentication.principal.id == #userId")
    @GetMapping("/{bookId}/delete/users/{userId}")
    public String deleteBook(@PathVariable("userId") Long userId,
                             @PathVariable("bookId") Long bookId){
        bookService.delete(bookId);
        return "redirect:/books/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public String getAllBooks(Model model,
                              @Param("title") String title){
        List<Book> books = bookService.findAllBooksByTitle(title).stream()
                .sorted(Comparator.comparing(book -> book.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("title", title);
        return "books-list";
    }
}