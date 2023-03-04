package com.system.libsystem.rest.books.details;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookDetailsService {

    private final BookService bookService;

    public BookEntity getBookDetails(int bookId) {
        return bookService.getBookById(bookId);
    }

}
