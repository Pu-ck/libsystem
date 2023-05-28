package com.system.libsystem.rest.administration.books.bookreturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnBookRequest {
    private Long borrowedBookId;
    private Long cardNumber;
}
