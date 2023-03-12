package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class FilterBooksController {

    private final FilterBooksService filterBooksService;

    @GetMapping
    public List<BookEntity> filterByBooksProperties(@RequestParam Map<String, String> requestParameters) {
        return filterBooksService.filterByBookProperties(requestParameters);
    }

    @GetMapping("/{book_id}")
    public BookEntity getBookDetails(@PathVariable("book_id") int bookId) {
        return filterBooksService.getBookDetails(bookId);
    }

}
