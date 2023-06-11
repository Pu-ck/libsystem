package com.system.libsystem.rest.userprofile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtendBookRequest {
    private Long borrowedBookId;
    private Long cardNumber;
}
