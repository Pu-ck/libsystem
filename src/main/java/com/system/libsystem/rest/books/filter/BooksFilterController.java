package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BooksFilterController {

    private final BooksFilterService booksFilterService;

    @PostMapping
    public BookEntity filter(@RequestBody BooksFilterRequest request) {
        return booksFilterService.filter(request);
    }

}
