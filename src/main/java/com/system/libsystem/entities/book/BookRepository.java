package com.system.libsystem.entities.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findAll();

    Optional<BookEntity> findById(int id);

    @Query("SELECT b FROM BookEntity b JOIN b.authors a JOIN b.publisherEntity p JOIN b.yearOfPrintEntity y " +
            "JOIN b.genres g JOIN b.affiliates af WHERE " +
            "(:title IS NULL OR :title = '' OR b.title = :title) AND " +
            "(:publisher IS NULL OR :publisher = '' OR p.name = :publisher) AND " +
            "(:yearOfPrint IS NULL OR :yearOfPrint = '' OR y.yearOfPrint = :yearOfPrint) AND " +
            "(:genre IS NULL OR :genre = '' OR g.name = :genre) AND " +
            "(:affiliate IS NULL OR :affiliate = '' OR af.name = :affiliate) AND " +
            "(:author IS NULL OR :author = '' OR a.name = :author)")
    List<BookEntity> findByTitlePublisherYearGenreAffiliateAndAuthor(@Param("title") String title,
                                                                     @Param("publisher") String publisher,
                                                                     @Param("yearOfPrint") String yearOfPrint,
                                                                     @Param("genre") String genre,
                                                                     @Param("affiliate") String affiliate,
                                                                     @Param("author") String author);
}
