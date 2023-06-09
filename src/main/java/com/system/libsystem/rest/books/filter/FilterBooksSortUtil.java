package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.author.AuthorEntity;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.genre.GenreEntity;
import com.system.libsystem.exceptions.book.BookNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class FilterBooksSortUtil {

    private static final String SORT_ASCENDING = "asc";
    private static final String SORT_DESCENDING = "desc";

    private final BookRepository bookRepository;

    public List<BookEntity> getBooksFilteredByTitleSorted(String sortDirection,
                                                          List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getTitle, Collections.reverseOrder()))
                    .toList();
        } else if (Objects.equals(sortDirection, SORT_ASCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getTitle)).toList();
        }
        return bookEntities;
    }

    public List<BookEntity> getBooksFilteredByAuthorSorted(String sortDirection,
                                                           List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing((BookEntity b) -> b.getAuthors().stream()
                                    .map(AuthorEntity::getName)
                                    .sorted()
                                    .collect(Collectors.joining(", ")))
                            .reversed())
                    .toList();
        } else if (Objects.equals(sortDirection, SORT_ASCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing((BookEntity b) -> b.getAuthors().stream()
                            .map(AuthorEntity::getName)
                            .sorted()
                            .collect(Collectors.joining(", "))))
                    .toList();
        }
        return bookEntities;
    }

    public List<BookEntity> getBooksFilteredByGenreSorted(String sortDirection,
                                                          List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing((BookEntity b) -> b.getGenres().stream()
                            .map(GenreEntity::getName)
                            .sorted()
                            .collect(Collectors.joining(", ")))
                            .reversed())
                            .toList();
        } else if (Objects.equals(sortDirection, SORT_ASCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing((BookEntity b) -> b.getGenres().stream()
                            .map(GenreEntity::getName)
                            .sorted()
                            .collect(Collectors.joining(", "))))
                    .toList();
        }
        return bookEntities;
    }

    public List<BookEntity> getBooksFilteredByPublisherSorted(String sortDirection,
                                                              List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(o -> o.getPublisherEntity().getName(), Collections.reverseOrder()))
                    .toList();
        } else if (Objects.equals(sortDirection, SORT_ASCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing(o -> o.getPublisherEntity().getName())).toList();
        }
        return  bookEntities;
    }

    public List<BookEntity> getBooksFilteredByYearOfPrintSorted(String sortDirection,
                                                                List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(o -> o.getYearOfPrintEntity().getYearOfPrint(), Collections.reverseOrder()))
                    .toList();
        } else if (Objects.equals(sortDirection, SORT_ASCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing(o -> o.getYearOfPrintEntity().getYearOfPrint())).toList();
        }
        return bookEntities;
    }

    public List<BookEntity> getBooksFilteredByCurrentQuantity(String sortDirection, List<BookEntity> bookEntities) {
        Stream<AffiliateBook> affiliateBookStream = getFilteredAffiliateBooks(bookEntities).stream();
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            affiliateBookStream = affiliateBookStream.sorted(Comparator.comparing(AffiliateBook::getCurrentQuantity,
                    Collections.reverseOrder()));
        } else {
            affiliateBookStream = affiliateBookStream.sorted(Comparator.comparing(AffiliateBook::getCurrentQuantity));
        }
        List<AffiliateBook> sortedAffiliateBooks = affiliateBookStream.toList();
        return getFilteredBooks(sortedAffiliateBooks).stream().distinct().toList();
    }

    public List<BookEntity> getBooksFilteredByGeneralQuantity(String sortDirection, List<BookEntity> bookEntities) {
        Stream<AffiliateBook> affiliateBookStream = getFilteredAffiliateBooks(bookEntities).stream();
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            affiliateBookStream = affiliateBookStream.sorted(Comparator.comparing(AffiliateBook::getGeneralQuantity,
                    Collections.reverseOrder()));
        } else {
            affiliateBookStream = affiliateBookStream.sorted(Comparator.comparing(AffiliateBook::getGeneralQuantity));
        }
        List<AffiliateBook> sortedAffiliateBooks = affiliateBookStream.toList();
        return getFilteredBooks(sortedAffiliateBooks).stream().distinct().toList();
    }

    public List<BookEntity> getBooksFilteredByAddDate(String sortDirection, List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getAddDate, Collections.reverseOrder()))
                    .toList();
        } else if (Objects.equals(sortDirection, SORT_ASCENDING)) {
            return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getAddDate)).toList();
        }
        return bookEntities;
    }

    private Set<AffiliateBook> getFilteredAffiliateBooks(List<BookEntity> bookEntities) {
        return bookEntities.stream().flatMap(bookEntity -> bookEntity.getAffiliateBooks().stream()).collect(Collectors.toSet());
    }

    private List<BookEntity> getFilteredBooks(List<AffiliateBook> affiliateBooks) {
        return affiliateBooks.stream()
                        .map(affiliateBook -> bookRepository.findById(affiliateBook.getBookId())
                        .orElseThrow(() -> new BookNotFoundException(affiliateBook.getBookId())))
                        .toList();
    }

}
