package com.system.libsystem.entities.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public Optional<BookEntity> getBookByBookProperty(String bookPropertyValue, String bookPropertyType) {
        if (Objects.equals(bookPropertyType, "title")) {
            return bookRepository.findByTitle(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "author")) {
            return bookRepository.findByAuthor(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "publisher")) {
            return bookRepository.findByPublisher(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "library")) {
            return bookRepository.findByLibrary(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "genre")) {
            return bookRepository.findByGenre(bookPropertyValue);
        } else if (Objects.equals(bookPropertyType, "yearOfPrint")) {
            return bookRepository.findByYearOfPrint(bookPropertyValue);
        }
        return Optional.empty();
    }

}
