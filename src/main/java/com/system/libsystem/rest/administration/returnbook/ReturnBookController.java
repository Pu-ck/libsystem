package com.system.libsystem.rest.administration.returnbook;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/home/administration/books/return")
public class ReturnBookController {

    private final ReturnBookService returnBookService;

    @PostMapping
    public void returnBook(@RequestBody ReturnBookRequest returnBookRequest) {
        returnBookService.returnBook(returnBookRequest);
    }

}
