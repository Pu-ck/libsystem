package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
                    .sorted(Comparator.comparing(BookEntity::getTitle).reversed())
                    .collect(Collectors.toList());
        }
        return bookEntities.stream()
                .sorted(Comparator.comparing(BookEntity::getTitle)).collect(Collectors.toList());
    }

    public List<BookEntity> getBooksFilteredByAuthorSorted(String sortDirection,
                                                           List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getAuthor).reversed())
                    .collect(Collectors.toList());
        }
        return bookEntities.stream()
                .sorted(Comparator.comparing(BookEntity::getAuthor)).collect(Collectors.toList());
    }

    public List<BookEntity> getBooksFilteredByGenreSorted(String sortDirection,
                                                          List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getGenre).reversed())
                    .collect(Collectors.toList());
        }
        return bookEntities.stream()
                .sorted(Comparator.comparing(BookEntity::getGenre)).collect(Collectors.toList());
    }

    public List<BookEntity> getBooksFilteredByPublisherSorted(String sortDirection,
                                                              List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getPublisher).reversed())
                    .collect(Collectors.toList());
        }
        return bookEntities.stream()
                .sorted(Comparator.comparing(BookEntity::getPublisher)).collect(Collectors.toList());
    }

    public List<BookEntity> getBooksFilteredByYearOfPrintSorted(String sortDirection,
                                                                List<BookEntity> bookEntities) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getYearOfPrint).reversed())
                    .collect(Collectors.toList());
        }
        return bookEntities.stream()
                .sorted(Comparator.comparing(BookEntity::getYearOfPrint)).collect(Collectors.toList());
    }

    public List<BookEntity> getBooksFilteredByCurrentQuantity(String sortDirection,
                                                              List<BookEntity> bookEntities,
                                                              String affiliate) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            if (affiliate.equals(AFFILIATE_A)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateA).reversed())
                        .collect(Collectors.toList());
            } else if (affiliate.equals(AFFILIATE_B)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateB).reversed())
                        .collect(Collectors.toList());
            }
        }
        if (affiliate.equals(AFFILIATE_A)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateA)).collect(Collectors.toList());
        } else if (affiliate.equals(AFFILIATE_B)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getCurrentQuantityAffiliateB)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<BookEntity> getBooksFilteredByGeneralQuantity(String sortDirection,
                                                              List<BookEntity> bookEntities,
                                                              String affiliate) {
        if (Objects.equals(sortDirection, SORT_DESCENDING)) {
            if (affiliate.equals(AFFILIATE_A)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateA).reversed())
                        .collect(Collectors.toList());
            } else if (affiliate.equals(AFFILIATE_B)) {
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateB).reversed())
                        .collect(Collectors.toList());
            }
        }
        if (affiliate.equals(AFFILIATE_A)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateA)).collect(Collectors.toList());
        } else if (affiliate.equals(AFFILIATE_B)) {
            return bookEntities.stream()
                    .sorted(Comparator.comparing(BookEntity::getGeneralQuantityAffiliateB)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
