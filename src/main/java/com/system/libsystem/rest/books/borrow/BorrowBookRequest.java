package com.system.libsystem.rest.books.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowBookRequest {
    private Long bookId;
    private Long cardNumber;
    private int quantity;
    private String affiliate;
}
