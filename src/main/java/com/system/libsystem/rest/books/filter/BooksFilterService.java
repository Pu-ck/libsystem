package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BooksFilterService {

    private final BookService bookService;
    private String filterPropertyValue;
    private String filterPropertyType;

    public BookEntity filter(BooksFilterRequest request) {

        if (request.getAuthor() != null) {
            filterPropertyValue = request.getAuthor();
            filterPropertyType = "author";
        } else if (request.getTitle() != null) {
            filterPropertyValue = request.getTitle();
            filterPropertyType = "title";
        } else if (request.getLibrary() != null) {
            filterPropertyValue = request.getLibrary();
            filterPropertyType = "library";
        } else if (request.getPublisher() != null) {
            filterPropertyValue = request.getPublisher();
            filterPropertyType = "publisher";
        } else if (request.getYearOfPrint() != null) {
            filterPropertyValue = request.getYearOfPrint();
            filterPropertyType = "yearOfPrint";
        }

        return bookService.getBookByBookProperty(filterPropertyValue, filterPropertyType).orElseThrow(() ->
                new IllegalStateException("Specified book not found"));
    }

}
