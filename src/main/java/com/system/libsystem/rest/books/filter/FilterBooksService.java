package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.system.libsystem.util.SharedConstants.AFFILIATE_A;
import static com.system.libsystem.util.SharedConstants.AFFILIATE_B;

@Service
@RequiredArgsConstructor
public class FilterBooksService {

    private static final String SORT_BY_TITLE = "1";
    private static final String SORT_BY_AUTHOR = "2";
    private static final String SORT_BY_GENRE = "3";
    private static final String SORT_BY_PUBLISHER = "4";
    private static final String SORT_BY_YEAR_OF_PRINT = "5";
    private static final String SORT_BY_CURRENT_QUANTITY = "6";
    private static final String SORT_BY_GENERAL_QUANTITY = "7";

    private final BookRepository bookRepository;
    private final FilterBooksSortUtil filterBooksSortUtil;
    private final BookService bookService;

    public BookEntity getBookDetails(int bookId) {
        return bookService.getBookById(bookId);
    }

    public List<BookEntity> filterByBookProperties(Map<String, String> requestParameters) {

        final String title = requestParameters.get("title");
        final String author = requestParameters.get("author");
        final String genre = requestParameters.get("genre");
        final String publisher = requestParameters.get("publisher");
        final String yearOfPrint = requestParameters.get("yearOfPrint");
        final String affiliate = requestParameters.get("affiliate");
        final String sortType = requestParameters.get("sortType");
        final String sortDirection = requestParameters.get("sortDirection");

        List<BookEntity> bookEntities = new ArrayList<>();

        if (title.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByTitle(title).stream()).collect(Collectors.toList());
        }
        if (author.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByAuthorContainingIgnoreCase(author).stream()).collect(Collectors.toList());
        }
        if (genre.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByGenreContainingIgnoreCase(genre).stream()).collect(Collectors.toList());
        }
        if (publisher.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByPublisher(publisher).stream()).collect(Collectors.toList());
        }
        if (yearOfPrint.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByYearOfPrint(yearOfPrint).stream()).collect(Collectors.toList());
        }
        if (affiliate.length() > 0) {
            bookEntities = getBooksFilteredByAffiliate(affiliate, bookEntities);
        }
        if (bookEntities.isEmpty()) {
            bookEntities = bookRepository.findAll();
        }

        return getSortedBooks(sortType, sortDirection, bookEntities.stream().distinct().collect(Collectors.toList()),
                affiliate);
    }

    private List<BookEntity> getBooksFilteredByAffiliate(String affiliate, List<BookEntity> bookEntities) {
        if (affiliate.equals(AFFILIATE_A)) {
            return Stream.concat(bookEntities.stream(),
                    bookRepository.findByCurrentQuantityAffiliateAGreaterThan(0)
                            .stream()).collect(Collectors.toList());
        } else if (affiliate.equals(AFFILIATE_B)) {
            return Stream.concat(bookEntities.stream(),
                    bookRepository.findByCurrentQuantityAffiliateBGreaterThan(0)
                            .stream()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<BookEntity> getSortedBooks(String sortType, String sortDirection, List<BookEntity> bookEntities,
                                            String affiliate) {
        switch (sortType) {
            case SORT_BY_TITLE:
                return filterBooksSortUtil.getBooksFilteredByTitleSorted(sortDirection, bookEntities);
            case SORT_BY_AUTHOR:
                return filterBooksSortUtil.getBooksFilteredByAuthorSorted(sortDirection, bookEntities);
            case SORT_BY_GENRE:
                return filterBooksSortUtil.getBooksFilteredByGenreSorted(sortDirection, bookEntities);
            case SORT_BY_PUBLISHER:
                return filterBooksSortUtil.getBooksFilteredByPublisherSorted(sortDirection, bookEntities);
            case SORT_BY_YEAR_OF_PRINT:
                return filterBooksSortUtil.getBooksFilteredByYearOfPrintSorted(sortDirection, bookEntities);
            case SORT_BY_CURRENT_QUANTITY:
                return filterBooksSortUtil.getBooksFilteredByCurrentQuantity(sortDirection, bookEntities, affiliate);
            case SORT_BY_GENERAL_QUANTITY:
                return filterBooksSortUtil.getBooksFilteredByGeneralQuantity(sortDirection, bookEntities, affiliate);
            default:
                return bookEntities;
        }
    }

}
