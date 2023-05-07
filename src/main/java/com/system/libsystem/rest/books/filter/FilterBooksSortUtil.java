package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.affiliatebook.AffiliateBookRepository;
import com.system.libsystem.entities.author.AuthorEntity;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.genre.GenreEntity;
import com.system.libsystem.exceptions.book.BookNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class FilterBooksSortUtil {

    private static final String SORT_DESCENDING = "1";
    private final AffiliateBookRepository affiliateBookRepository;
    private final BookRepository bookRepository;

    public List<BookEntity> getBooksFilteredByTitleSorted(String sortDirection,
                                                          List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getTitle, Collections.reverseOrder()))
                    .toList();
        }
        return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getTitle)).toList();
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
        }
        return bookEntities.stream().sorted(Comparator.comparing((BookEntity b) -> b.getAuthors().stream()
                    .map(AuthorEntity::getName)
                    .sorted()
                    .collect(Collectors.joining(", "))))
                    .toList();
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
        }
        return bookEntities.stream().sorted(Comparator.comparing((BookEntity b) -> b.getGenres().stream()
                    .map(GenreEntity::getName)
                    .sorted()
                    .collect(Collectors.joining(", "))))
                    .toList();
    }

    public List<BookEntity> getBooksFilteredByPublisherSorted(String sortDirection,
                                                              List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(o -> o.getPublisherEntity().getName(), Collections.reverseOrder()))
                    .toList();
        }
        return bookEntities.stream().sorted(Comparator.comparing(o -> o.getPublisherEntity().getName())).toList();
    }

    public List<BookEntity> getBooksFilteredByYearOfPrintSorted(String sortDirection,
                                                                List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(o -> o.getYearOfPrintEntity().getYearOfPrint(), Collections.reverseOrder()))
                    .toList();
        }
        return bookEntities.stream().sorted(Comparator.comparing(o -> o.getYearOfPrintEntity().getYearOfPrint())).toList();
    }

    public List<BookEntity> getBooksFilteredByCurrentQuantity(String sortDirection) {

        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            final List<AffiliateBook> affiliateBooks = affiliateBookRepository.findAll().stream()
                    .sorted(Comparator.comparing(AffiliateBook::getCurrentQuantity, Collections.reverseOrder()))
                    .toList();
            return getFilteredBooks(affiliateBooks).stream().distinct().toList();
        }

        final List<AffiliateBook> affiliateBooks = affiliateBookRepository.findAll().stream()
                .sorted(Comparator.comparing(AffiliateBook::getCurrentQuantity)).toList();
        return getFilteredBooks(affiliateBooks).stream().distinct().toList();
    }

    public List<BookEntity> getBooksFilteredByGeneralQuantity(String sortDirection) {

        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            final List<AffiliateBook> affiliateBooks = affiliateBookRepository.findAll().stream()
                    .sorted(Comparator.comparing(AffiliateBook::getGeneralQuantity, Collections.reverseOrder()))
                    .toList();
            return getFilteredBooks(affiliateBooks).stream().distinct().toList();
        }

        final List<AffiliateBook> affiliateBooks = affiliateBookRepository.findAll().stream()
                .sorted(Comparator.comparing(AffiliateBook::getGeneralQuantity)).toList();
        return getFilteredBooks(affiliateBooks).stream().distinct().toList();
    }

    private List<BookEntity> getFilteredBooks(List<AffiliateBook> affiliateBooks) {
        return affiliateBooks.stream()
                        .map(affiliateBook -> bookRepository.findById(affiliateBook.getBookId())
                        .orElseThrow(() -> new BookNotFoundException(affiliateBook.getBookId())))
                        .toList();
    }

}
