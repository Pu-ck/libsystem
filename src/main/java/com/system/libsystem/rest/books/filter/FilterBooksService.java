package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        final List<String> filterParameters = Arrays.asList(title, author, genre, publisher, yearOfPrint, affiliate);

        List<BookEntity> bookEntities;

        if (isFilterEmpty(filterParameters)) {
            bookEntities = bookRepository.findAll();
        } else {
            bookEntities = bookRepository.findByTitlePublisherYearGenreAffiliateAndAuthor(title, publisher, yearOfPrint,
                    genre, affiliate, author);
        }

        return getSortedBooks(sortType, sortDirection, bookEntities.stream().distinct().toList());
    }

    private List<BookEntity> getSortedBooks(String sortType, String sortDirection, List<BookEntity> bookEntities) {
        return switch (sortType) {
            case SORT_BY_TITLE -> filterBooksSortUtil.getBooksFilteredByTitleSorted(sortDirection, bookEntities);
            case SORT_BY_AUTHOR -> filterBooksSortUtil.getBooksFilteredByAuthorSorted(sortDirection, bookEntities);
            case SORT_BY_GENRE -> filterBooksSortUtil.getBooksFilteredByGenreSorted(sortDirection, bookEntities);
            case SORT_BY_PUBLISHER ->
                    filterBooksSortUtil.getBooksFilteredByPublisherSorted(sortDirection, bookEntities);
            case SORT_BY_YEAR_OF_PRINT ->
                    filterBooksSortUtil.getBooksFilteredByYearOfPrintSorted(sortDirection, bookEntities);
            case SORT_BY_CURRENT_QUANTITY -> filterBooksSortUtil.getBooksFilteredByCurrentQuantity(sortDirection);
            case SORT_BY_GENERAL_QUANTITY -> filterBooksSortUtil.getBooksFilteredByGeneralQuantity(sortDirection);
            default -> bookEntities;
        };
    }

    private boolean isFilterEmpty(final List<String> filterParameters) {
        return filterParameters.get(0).length() == 0
                && filterParameters.get(1).length() == 0
                && filterParameters.get(2).length() == 0
                && filterParameters.get(3).length() == 0
                && filterParameters.get(4).length() == 0
                && filterParameters.get(5).length() == 0;
    }

}
