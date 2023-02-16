package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksFilterService {

    private final BookService bookService;
    private String filterPropertyValue;
    private String filterPropertyType;

    public List<BookEntity> filterByBookProperty(BooksFilterRequest request) {

        if (request.getTitle().length() > 0) {
            filterPropertyValue = request.getTitle();
            filterPropertyType = "title";
        } else if (request.getAuthor().length() > 0) {
            filterPropertyValue = request.getAuthor();
            filterPropertyType = "author";
        } else if (request.getGenre().length() > 0) {
            filterPropertyValue = request.getGenre();
            filterPropertyType = "genre";
        } else if (request.getPublisher().length() > 0) {
            filterPropertyValue = request.getPublisher();
            filterPropertyType = "publisher";
        } else if (request.getYearOfPrint().length() > 0) {
            filterPropertyValue = request.getYearOfPrint();
            filterPropertyType = "yearOfPrint";
        }

        return bookService.getBookByBookProperty(filterPropertyValue, filterPropertyType);
    }

}
