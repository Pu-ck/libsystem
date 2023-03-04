package com.system.libsystem.entities.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public BookEntity getBookById(int bookId) throws IllegalStateException {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new IllegalStateException("Unable to find book with id " + bookId));
    }

}
