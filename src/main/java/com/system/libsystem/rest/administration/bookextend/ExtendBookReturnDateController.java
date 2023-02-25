package com.system.libsystem.rest.administration.bookextend;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/books/extend-book")
public class ExtendBookReturnDateController {

    private final ExtendBookReturnDateService extendBookReturnDateService;

    @PostMapping
    public void extendRequestedBookReturnDate(@RequestBody ExtendBookReturnDateRequest extendBookReturnDateRequest) {
        extendBookReturnDateService.extendRequestedBookReturnDate(extendBookReturnDateRequest);
    }

}
