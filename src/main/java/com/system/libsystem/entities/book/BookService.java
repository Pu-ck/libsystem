package com.system.libsystem.entities.book;

import com.system.libsystem.exceptions.book.BookNotFoundException;

public interface BookService {

    BookEntity getBookById(Long bookId) throws BookNotFoundException;

}
