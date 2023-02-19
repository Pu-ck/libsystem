package com.system.libsystem.helpermodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBook {
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private String yearOfPrint;
    private String borrowDate;
    private String returnDate;
    private String penalty;
}
