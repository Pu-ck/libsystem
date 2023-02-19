package com.system.libsystem.rest.books.borrow;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/home/books")
public class BooksBorrowController {

    private final BooksBorrowService booksBorrowService;

    @PostMapping
    public void borrow(@RequestBody BooksBorrowRequest booksBorrowRequest, HttpServletRequest httpServletRequest) {
        booksBorrowService.borrow(booksBorrowRequest, httpServletRequest);
    }

}
