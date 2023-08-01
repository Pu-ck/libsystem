package com.system.libsystem.rest.administration.books;

import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;

import java.util.List;

public interface BorrowedBooksService {

    List<BorrowedBookEntity> getBorrowedBooks();

    List<BorrowedBookEntity> getBorrowedBooksById(Long borrowedBookId);

    List<BorrowedBookEntity> getBorrowedBooksByCardNumber(Long cardNumber);

    List<BorrowedBookEntity> getBorrowedBooksByUserId(Long userId);

    List<BorrowedBookEntity> getBorrowedBooksByBookId(Long bookId);

}
