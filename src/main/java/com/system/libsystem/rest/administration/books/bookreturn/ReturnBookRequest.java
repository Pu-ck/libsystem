package com.system.libsystem.rest.administration.books.bookreturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReturnBookRequest {
    private Long borrowedBookId;
    private Long cardNumber;
}
