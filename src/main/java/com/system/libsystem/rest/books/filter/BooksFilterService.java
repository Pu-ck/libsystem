package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BooksFilterService {

    private final BookRepository bookRepository;

    public List<BookEntity> filterByBookProperties(Map<String, String> requestParameters) {

        final String title = requestParameters.get("title");
        final String author = requestParameters.get("author");
        final String genre = requestParameters.get("genre");
        final String publisher = requestParameters.get("publisher");
        final String yearOfPrint = requestParameters.get("yearOfPrint");

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

        return bookEntities;
    }

}
