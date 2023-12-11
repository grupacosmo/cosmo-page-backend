package com.webdev.cosmo.cosmobackend.obsolete.repository;

import com.webdev.cosmo.cosmobackend.obsolete.schemas.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
