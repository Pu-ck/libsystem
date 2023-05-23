package com.system.libsystem.entities.book;

import com.system.libsystem.exceptions.book.BookNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public BookEntity getBookById(Long bookId) throws BookNotFoundException {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }

}
