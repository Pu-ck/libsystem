package com.system.libsystem.entities.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public List<BookEntity> getBookByBookProperty(String bookPropertyValue, String bookPropertyType) {
        if (Objects.equals(bookPropertyType, "title")) {
            return bookRepository.findByTitle(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "author")) {
            return bookRepository.findByAuthorContainingIgnoreCase(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "genre")) {
            return bookRepository.findByGenreContainingIgnoreCase(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "publisher")) {
            return bookRepository.findByPublisher(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "yearOfPrint")) {
            return bookRepository.findByYearOfPrint(bookPropertyValue);
        }
        return Collections.emptyList();
    }

}
