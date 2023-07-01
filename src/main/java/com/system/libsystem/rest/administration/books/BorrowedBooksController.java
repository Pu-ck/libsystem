package com.system.libsystem.rest.administration.books;

import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books")
public class BorrowedBooksController {

    private final BorrowedBooksService borrowedBooksService;

    @GetMapping
    public List<BorrowedBookEntity> getBorrowedBooks(HttpServletRequest httpServletRequest) {
        return borrowedBooksService.getBorrowedBooks(httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "borrowedBookId")
    public List<BorrowedBookEntity> getBorrowedBookById(@RequestParam(required = false) Long borrowedBookId,
                                                        HttpServletRequest httpServletRequest) {
        return borrowedBooksService.getBorrowedBooksById(borrowedBookId, httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "cardNumber")
    public List<BorrowedBookEntity> getBorrowedBookByCardNumber(@RequestParam(required = false) Long cardNumber,
                                                                HttpServletRequest httpServletRequest) {
        return borrowedBooksService.getBorrowedBooksByCardNumber(cardNumber, httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "userId")
    public List<BorrowedBookEntity> getBorrowedBooksByUserId(@RequestParam(required = false) Long userId,
                                                             HttpServletRequest httpServletRequest) {
        return borrowedBooksService.getBorrowedBooksByUserId(userId, httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "bookId")
    public List<BorrowedBookEntity> getBorrowedBooksByBookId(@RequestParam(required = false) Long bookId,
                                                             HttpServletRequest httpServletRequest) {
        return borrowedBooksService.getBorrowedBooksByBookId(bookId, httpServletRequest);
    }

}
