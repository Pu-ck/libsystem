package com.system.libsystem.rest.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExtendBookRequest {
    private int borrowedBookId;
    private Long cardNumber;
}
