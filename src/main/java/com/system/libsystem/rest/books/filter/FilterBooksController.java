package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;

import com.system.libsystem.rest.books.borrow.BorrowBookRequest;
import com.system.libsystem.rest.books.borrow.BorrowBookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(params = "id")
    public BookEntity getBookDetails(@RequestParam("id") int bookId) {
        return filterBooksService.getBookDetails(bookId);
    }

}
