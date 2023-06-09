package com.system.libsystem.rest.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewBookDTO {
    private Long bookId;
    private String yearOfPrint;
    private String publisher;
    private String authors;
    private String title;
    private String genres;
}
