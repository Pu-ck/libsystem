package com.system.libsystem.entities.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findByTitle(String title);

    List<BookEntity> findByAuthorContainingIgnoreCase(String author);

    List<BookEntity> findByGenreContainingIgnoreCase(String genre);

    List<BookEntity> findByPublisher(String publisher);

    List<BookEntity> findByYearOfPrint(String yearOfPrint);
}
