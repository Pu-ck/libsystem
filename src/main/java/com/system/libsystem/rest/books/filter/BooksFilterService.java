package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BooksFilterService {

    private static final String SORT_BY_TITLE = "1";
    private static final String SORT_BY_AUTHOR = "2";
    private static final String SORT_BY_GENRE = "3";
    private static final String SORT_BY_PUBLISHER = "4";
    private static final String SORT_BY_YEAR_OF_PRINT = "5";
    private static final String SORT_BY_QUANTITY = "6";

    private final BookRepository bookRepository;

    public List<BookEntity> filterByBookProperties(Map<String, String> requestParameters) {

        final String title = requestParameters.get("title");
        final String author = requestParameters.get("author");
        final String genre = requestParameters.get("genre");
        final String publisher = requestParameters.get("publisher");
        final String yearOfPrint = requestParameters.get("yearOfPrint");
        final String sortType = requestParameters.get("sortType");

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

        return getSortedBooks(sortType, bookEntities);
    }

    private List<BookEntity> getSortedBooks(String sortType, List<BookEntity> bookEntities) {
        switch (sortType) {
            case SORT_BY_TITLE:
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getTitle)).collect(Collectors.toList());
            case SORT_BY_AUTHOR:
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getAuthor)).collect(Collectors.toList());
            case SORT_BY_GENRE:
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getGenre)).collect(Collectors.toList());
            case SORT_BY_PUBLISHER:
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getPublisher)).collect(Collectors.toList());
            case SORT_BY_YEAR_OF_PRINT:
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getYearOfPrint)).collect(Collectors.toList());
            case SORT_BY_QUANTITY:
                return bookEntities.stream()
                        .sorted(Comparator.comparing(BookEntity::getQuantity)).collect(Collectors.toList());
            default:
                return bookEntities;
        }
    }

}
