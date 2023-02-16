package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BooksFilterController {

    private final BooksFilterService booksFilterService;

    @GetMapping
    public List<BookEntity> filterByBookProperty(@RequestBody BooksFilterRequest request) {
        return booksFilterService.filterByBookProperty(request);
    }

}
