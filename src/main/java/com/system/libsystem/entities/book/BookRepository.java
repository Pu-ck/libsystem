package com.system.libsystem.entities.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByTitle(String title);

    Optional<BookEntity> findByAuthor(String author);

    Optional<BookEntity> findByGenre(String genre);

    Optional<BookEntity> findByLibrary(String library);

    Optional<BookEntity> findByPublisher(String publisher);

    Optional<BookEntity> findByYearOfPrint(String yearOfPrint);
}
