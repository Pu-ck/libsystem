package com.system.libsystem.rest.books.filter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class BooksFilterRequest {
    private final String title;
    private final String author;
    private final String genre;
    private final String publisher;
    private final String library;
    private final String yearOfPrint;
}