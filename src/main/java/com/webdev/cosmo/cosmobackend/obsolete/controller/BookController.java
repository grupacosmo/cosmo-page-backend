package com.webdev.cosmo.cosmobackend.obsolete.controller;

import com.webdev.cosmo.cosmobackend.obsolete.repository.BookRepository;
import com.webdev.cosmo.cosmobackend.obsolete.schemas.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping
    public Flux<Book> getHome() {
        return bookRepository.findAll();
    }

    @PostMapping
    public Mono<Book> postBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping
    public Mono<Book> updateBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping
    public boolean deleteBook(@RequestBody Book book) {
        bookRepository.deleteById(book.getId()).block();
        return true;
    }
}
