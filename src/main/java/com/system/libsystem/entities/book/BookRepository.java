package com.system.libsystem.entities.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findById(Long id);

    @Query("SELECT b FROM BookEntity b JOIN b.authors a JOIN b.publisherEntity p JOIN b.yearOfPrintEntity y " +
            "LEFT JOIN b.genres g JOIN b.affiliates af WHERE " +
            "(:title IS NULL OR :title = '' OR b.title LIKE %:title%) AND " +
            "(:author IS NULL OR :author = '' OR a.name LIKE %:author%) AND " +
            "(:publisher IS NULL OR :publisher = '' OR p.name LIKE %:publisher%) AND " +
            "(:startYear IS NULL OR :startYear = '' OR y.yearOfPrint >= :startYear) AND " +
            "(:endYear IS NULL OR :endYear = '' OR y.yearOfPrint <= :endYear) AND " +
            "(COALESCE(:genres, null) IS NULL OR COALESCE(:genres, null) = '' OR g.name IN (:genres)) AND " +
            "(COALESCE(:affiliates, null) IS NULL OR COALESCE(:affiliates, null) = '' OR af.name IN (:affiliates))")
    List<BookEntity> findByTitleAuthorPublisherYearGenresAndAffiliates(@Param("title") String title,
                                                                       @Param("author") String author,
                                                                       @Param("publisher") String publisher,
                                                                       @Param("startYear") String startYear,
                                                                       @Param("endYear") String endYear,
                                                                       @Param("genres") List<String> genres,
                                                                       @Param("affiliates") List<String> affiliates);
}
