package com.system.libsystem.rest.administration.books;

import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books")
public class BorrowedBooksController {

    private final BorrowedBooksService borrowedBooksService;

    @GetMapping
    public List<BorrowedBookEntity> getBorrowedBooks() {
        return borrowedBooksService.getBorrowedBooks();
    }

    @GetMapping
    @RequestMapping(params = "borrowedBookId")
    public List<BorrowedBookEntity> getBorrowedBookById(@RequestParam(required = false) Long borrowedBookId) {
        return borrowedBooksService.getBorrowedBooksById(borrowedBookId);
    }

    @GetMapping
    @RequestMapping(params = "cardNumber")
    public List<BorrowedBookEntity> getBorrowedBookByCardNumber(@RequestParam(required = false) Long cardNumber) {
        return borrowedBooksService.getBorrowedBooksByCardNumber(cardNumber);
    }

    @GetMapping
    @RequestMapping(params = "userId")
    public List<BorrowedBookEntity> getBorrowedBooksByUserId(@RequestParam(required = false) Long userId) {
        return borrowedBooksService.getBorrowedBooksByUserId(userId);
    }

    @GetMapping
    @RequestMapping(params = "bookId")
    public List<BorrowedBookEntity> getBorrowedBooksByBookId(@RequestParam(required = false) Long bookId) {
        return borrowedBooksService.getBorrowedBooksByBookId(bookId);
    }

}
