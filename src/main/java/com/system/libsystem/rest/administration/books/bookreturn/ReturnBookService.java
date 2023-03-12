package com.system.libsystem.rest.administration.books.bookreturn;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.rest.util.BookUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.system.libsystem.util.SharedConstants.BOOK_ALREADY_ACCEPTED_LOG;
import static com.system.libsystem.util.SharedConstants.INVALID_CARD_NUMBER_LOG;

@Service
@AllArgsConstructor
@Slf4j
public class ReturnBookService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookUtil bookUtil;
    private final BookRepository bookRepository;
    private final BorrowedBookService borrowedBookService;
    private final BookService bookService;
    private final UserService userService;

    public void returnBook(ReturnBookRequest returnBookRequest) {

        BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(returnBookRequest
                .getBorrowedBookId());
        BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());

        if (!borrowedBookEntity.isClosed()) {
            if (bookUtil.isCardNumberValid(userEntity.getCardNumber(), returnBookRequest.getCardNumber())) {
                returnBorrowedBook(borrowedBookEntity, bookEntity);
            } else {
                throw new IllegalStateException(INVALID_CARD_NUMBER_LOG);
            }
        } else {
            throw new IllegalStateException(BOOK_ALREADY_ACCEPTED_LOG + borrowedBookEntity.getId());
        }
    }

    private void returnBorrowedBook(BorrowedBookEntity borrowedBookEntity, BookEntity bookEntity) {
        borrowedBookEntity.setClosed(true);
        borrowedBookRepository.save(borrowedBookEntity);
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, borrowedBookEntity.getAffiliate(), 1);
        bookRepository.save(bookEntity);
        log.info("The borrowed book with id " + borrowedBookEntity.getId() + " has been returned and set as closed " +
                "in the repository");
    }

}
