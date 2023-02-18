package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/home/books")
public class BooksFilterController {

    private final BooksFilterService booksFilterService;

    @GetMapping
    @ResponseBody
    public List<BookEntity> filterByBooksProperties(@RequestParam Map<String, String> requestParameters) {
        return booksFilterService.filterByBookProperties(requestParameters);
    }

}
