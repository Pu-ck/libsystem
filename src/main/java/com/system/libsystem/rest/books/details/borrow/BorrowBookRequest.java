package com.system.libsystem.rest.books.details.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BorrowBookRequest {
    private int bookId;
    private Long cardNumber;
    private int quantity;
    private String affiliate;
}
