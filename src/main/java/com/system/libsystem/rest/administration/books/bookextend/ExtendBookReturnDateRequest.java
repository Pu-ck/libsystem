package com.system.libsystem.rest.administration.books.bookextend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExtendBookReturnDateRequest {
    private int borrowedBookId;
    private int extendTime;
}
