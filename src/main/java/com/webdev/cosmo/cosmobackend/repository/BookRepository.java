package com.webdev.cosmo.cosmobackend.repository;

import com.webdev.cosmo.cosmobackend.schemas.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ReactiveCrudRepository<Book, Long> {
}
