package com.system.libsystem.rest.books.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BooksBorrowRequest {
    private int bookId;
    private Long cardNumber;
    private int quantity;
}
