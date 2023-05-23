package com.system.libsystem.rest.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExtendBookRequest {
    private Long borrowedBookId;
    private Long cardNumber;
}
