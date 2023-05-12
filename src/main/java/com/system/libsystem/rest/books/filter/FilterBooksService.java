package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private static final int EMPTY_LIST_TO_STRING_LENGTH = 2;

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

        final List<String> genres = Arrays.asList(requestParameters.get("genres").split(","));
        final List<String> affiliates = Arrays.asList(requestParameters.get("affiliates").split(","));

        final Map<String, String> filterParameters = Map.of("title", title, "author", author,
                "publisher", publisher, "yearOfPrint", yearOfPrint, "genres", genres.toString(),
                "affiliates", affiliates.toString());

        List<BookEntity> bookEntities;

        if (isFilterEmpty(filterParameters)) {
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

    private boolean isFilterEmpty(final Map<String, String> filterParameters) {
        return filterParameters.get("title").length() == 0
                && filterParameters.get("author").length() == 0
                && filterParameters.get("publisher").length() == 0
                && filterParameters.get("yearOfPrint").length() == 0
                && filterParameters.get("genres").length() == EMPTY_LIST_TO_STRING_LENGTH
                && filterParameters.get("affiliates").length() == EMPTY_LIST_TO_STRING_LENGTH;
    }

}