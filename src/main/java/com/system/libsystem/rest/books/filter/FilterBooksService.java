package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilterBooksService {

    private static final String SORT_BY_TITLE = "title";
    private static final String SORT_BY_AUTHOR = "author";
    private static final String SORT_BY_GENRE = "genre";
    private static final String SORT_BY_PUBLISHER = "publisher";
    private static final String SORT_BY_YEAR_OF_PRINT = "year";
    private static final String SORT_BY_CURRENT_QUANTITY = "currentQuantity";
    private static final String SORT_BY_GENERAL_QUANTITY = "generalQuantity";

    private final BookRepository bookRepository;
    private final FilterBooksSortUtil filterBooksSortUtil;
    private final BookService bookService;

    public BookEntity getBookDetails(int bookId) {
        return bookService.getBookById(bookId);
    }

    public List<BookEntity> filterByBookProperties(Map<String, String> requestParameters) {
        final String title = Optional.ofNullable(requestParameters.get("title")).orElse("");
        final String author = Optional.ofNullable(requestParameters.get("author")).orElse("");
        final String publisher = Optional.ofNullable(requestParameters.get("publisher")).orElse("");
        final String yearOfPrint = Optional.ofNullable(requestParameters.get("yearOfPrint")).orElse("");
        final String sortType = Optional.ofNullable(requestParameters.get("sortType")).orElse("");
        final String sortDirection = Optional.ofNullable(requestParameters.get("sortDirection")).orElse("");

        List<String> genres = new ArrayList<>();
        List<String> affiliates = new ArrayList<>();
        setFilterListParameters(requestParameters, genres, affiliates);

        final List<String> filterParameters = Arrays.asList(title, author, publisher, yearOfPrint);
        final List<List<String>> filterListParameters = Arrays.asList(genres, affiliates);
        List<BookEntity> bookEntities;

        if (isFilterEmpty(filterParameters, filterListParameters)) {
            bookEntities = bookRepository.findAll();
        } else {
            bookEntities = bookRepository.findByTitlePublisherYearGenreAffiliateAndAuthor(title, author, publisher,
                    yearOfPrint, genres, affiliates);
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

    private void setFilterListParameters(Map<String, String> requestParameters, List<String> genres, List<String> affiliates) {
        final String genresParameter = Optional.ofNullable(requestParameters.get("genres")).orElse("");
        final String affiliatesParameter = Optional.ofNullable(requestParameters.get("affiliates")).orElse("");
        if (!genresParameter.equals("")) {
            genres.addAll(Arrays.asList(genresParameter.split(",")));
        }
        if (!affiliatesParameter.equals("")) {
            affiliates.addAll(Arrays.asList(affiliatesParameter.split(",")));
        }
    }

    private boolean isFilterEmpty(final List<String> filterParameters, List<List<String>> filterListParameters) {
        return filterParameters.get(0).length() == 0
                && filterParameters.get(1).length() == 0
                && filterParameters.get(2).length() == 0
                && filterParameters.get(3).length() == 0
                && filterListParameters.get(0).isEmpty()
                && filterListParameters.get(1).isEmpty();
    }

}