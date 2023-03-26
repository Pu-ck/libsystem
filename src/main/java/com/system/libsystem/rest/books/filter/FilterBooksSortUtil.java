package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.author.AuthorEntity;
import com.system.libsystem.entities.genre.GenreEntity;
import com.system.libsystem.entities.book.BookEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.system.libsystem.util.SharedConstants.AFFILIATE_A;
import static com.system.libsystem.util.SharedConstants.AFFILIATE_B;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class FilterBooksSortUtil {

    private static final String SORT_DESCENDING = "1";

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
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getAuthors, Collections.reverseOrder()))
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
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getGenres, Collections.reverseOrder()))
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

    public List<BookEntity> getBooksFilteredByCurrentQuantity(String sortDirection,
                                                              List<BookEntity> bookEntities,
                                                              String affiliate) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            if (affiliate.equals(AFFILIATE_A)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateA, Collections.reverseOrder()))
                        .toList();
            } else if (affiliate.equals(AFFILIATE_B)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateB, Collections.reverseOrder()))
                        .toList();
            }
        }
        if (affiliate.equals(AFFILIATE_A)) {
            return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateA)).toList();
        } else if (affiliate.equals(AFFILIATE_B)) {
            return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateB)).toList();
        }
        return Collections.emptyList();
    }

    public List<BookEntity> getBooksFilteredByGeneralQuantity(String sortDirection,
                                                              List<BookEntity> bookEntities,
                                                              String affiliate) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            if (affiliate.equals(AFFILIATE_A)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateA, Collections.reverseOrder()))
                        .toList();
            } else if (affiliate.equals(AFFILIATE_B)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateB, Collections.reverseOrder()))
                        .toList();
            }
        }
        if (affiliate.equals(AFFILIATE_A)) {
            return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateA)).toList();
        } else if (affiliate.equals(AFFILIATE_B)) {
            return bookEntities.stream().sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateB)).toList();
        }
        return Collections.emptyList();
    }

}
