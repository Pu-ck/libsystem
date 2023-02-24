package com.system.libsystem.rest.books.details;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.system.libsystem.util.SharedConstants.FIND_BOOK_EXCEPTION_LOG;

@Service
@AllArgsConstructor
public class BookDetailsService {

    private final BookRepository bookRepository;

    public BookEntity getBookDetails(int bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG + bookId));
    }

}
