package com.system.libsystem.entities.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findByTitle(String title);

    @Query("SELECT b FROM BookEntity b JOIN b.authors g WHERE g.name = :authorName")
    List<BookEntity> findByAuthorName(@Param("authorName") String authorName);

    @Query("SELECT b FROM BookEntity b JOIN b.genres g WHERE g.name = :genreName")
    List<BookEntity> findByGenreName(@Param("genreName") String genreName);

    @Query("SELECT b FROM BookEntity b JOIN b.publisherEntity p WHERE p.name = :publisherName")
    List<BookEntity> findByPublisherName(@Param("publisherName") String publisherName);

    @Query("SELECT b FROM BookEntity b JOIN b.yearOfPrintEntity p WHERE p.yearOfPrint = :yearOfPrint")
    List<BookEntity> findByYearOfPrint(@Param("yearOfPrint") int yearOfPrint);

    List<BookEntity> findByCurrentQuantityAffiliateAGreaterThan(int value);
    List<BookEntity> findByCurrentQuantityAffiliateBGreaterThan(int value);

    List<BookEntity> findAll();

    Optional<BookEntity> findById(int id);

}
