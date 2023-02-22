package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FilterBooksService {

    private static final String SORT_BY_TITLE = "1";
    private static final String SORT_BY_AUTHOR = "2";
    private static final String SORT_BY_GENRE = "3";
    private static final String SORT_BY_PUBLISHER = "4";
    private static final String SORT_BY_YEAR_OF_PRINT = "5";
    private static final String SORT_BY_QUANTITY = "6";
    private static final String SORT_DESCENDING = "1";

    private final BookRepository bookRepository;

    public List<BookEntity> filterByBookProperties(Map<String, String> requestParameters) {

        final String title = requestParameters.get("title");
        final String author = requestParameters.get("author");
        final String genre = requestParameters.get("genre");
        final String publisher = requestParameters.get("publisher");
        final String yearOfPrint = requestParameters.get("yearOfPrint");
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
        if (bookEntities.isEmpty()) {
            bookEntities = bookRepository.findAll();
        }

        return getSortedBooks(sortType, sortDirection, bookEntities);
    }

    private List<BookEntity> getSortedBooks(String sortType, String sortDirection, List<BookEntity> bookEntities) {
        switch (sortType) {
            case SORT_BY_TITLE:
                if (Objects.equals(sortDirection, SORT_DESCENDING)) {
                    return bookEntities.stream()
                            .sorted(Comparator.comparing(BookEntity::getTitle).reversed())
                            .collect(Collectors.toList());
                }
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getTitle)).collect(Collectors.toList());
            case SORT_BY_AUTHOR:
                if (Objects.equals(sortDirection, SORT_DESCENDING)) {
                    return bookEntities.stream()
                            .sorted(Comparator.comparing(BookEntity::getAuthor).reversed())
                            .collect(Collectors.toList());
                }
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getAuthor)).collect(Collectors.toList());
            case SORT_BY_GENRE:
                if (Objects.equals(sortDirection, SORT_DESCENDING)) {
                    return bookEntities.stream()
                            .sorted(Comparator.comparing(BookEntity::getGenre).reversed())
                            .collect(Collectors.toList());
                }
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getGenre)).collect(Collectors.toList());
            case SORT_BY_PUBLISHER:
                if (Objects.equals(sortDirection, SORT_DESCENDING)) {
                    return bookEntities.stream()
                            .sorted(Comparator.comparing(BookEntity::getPublisher).reversed())
                            .collect(Collectors.toList());
                }
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getPublisher)).collect(Collectors.toList());
            case SORT_BY_YEAR_OF_PRINT:
                if (Objects.equals(sortDirection, SORT_DESCENDING)) {
                    return bookEntities.stream()
                            .sorted(Comparator.comparing(BookEntity::getYearOfPrint).reversed())
                            .collect(Collectors.toList());
                }
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getYearOfPrint)).collect(Collectors.toList());
            case SORT_BY_QUANTITY:
                if (Objects.equals(sortDirection, SORT_DESCENDING)) {
                    return bookEntities.stream()
                            .sorted(Comparator.comparing(BookEntity::getQuantity).reversed())
                            .collect(Collectors.toList());
                }
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getQuantity)).collect(Collectors.toList());
            default:
                return bookEntities;
        }
    }

}
