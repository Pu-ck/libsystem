package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.book.BookEntity;

import java.util.List;
import java.util.Map;

public interface FilterBooksService {

    List<BookEntity> filterByBookProperties(Map<String, String> requestParameters);

    BookEntity getBookDetails(Long bookId);

}
