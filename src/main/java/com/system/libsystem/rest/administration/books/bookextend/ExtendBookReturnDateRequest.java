package com.system.libsystem.rest.administration.books.bookextend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtendBookReturnDateRequest {
    private Long borrowedBookId;
    private int extendTime;
}
