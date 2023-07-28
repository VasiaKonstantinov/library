package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public Book create(Book book) {
        if(book == null){
            throw new RuntimeException("is null");
        }
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Not found");
        });
    }

    public Book update(Book book) {
        if(book == null){
            throw new RuntimeException("Is null");
        }
        findById(book.getId());
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        bookRepository.delete(findById(id));
    }

    public List<Book> findAllBooksByTitle(String title) {
        return (title == null || title.isEmpty()) ? bookRepository.findAll() : bookRepository.findAllByTitle(title);
    }

}