package com.system.libsystem.rest.administration.books.bookreturn;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.book.BookAlreadyReturnedException;
import com.system.libsystem.exceptions.cardnumber.InvalidCardNumberFormatException;
import com.system.libsystem.rest.util.BookUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ReturnBookService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final UserRepository userRepository;
    private final BookUtil bookUtil;
    private final BookRepository bookRepository;
    private final BorrowedBookService borrowedBookService;
    private final BookService bookService;
    private final UserService userService;

    @Transactional
    public void returnBook(ReturnBookRequest returnBookRequest) {

        BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(returnBookRequest
                .getBorrowedBookId());
        BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());

        if (borrowedBookEntity.isClosed()) {
            throw new BookAlreadyReturnedException(borrowedBookEntity.getId());
        }
        if (!bookUtil.isCardNumberValid(userEntity.getCardNumber(), returnBookRequest.getCardNumber())) {
            throw new InvalidCardNumberFormatException();
        }
        returnBorrowedBook(borrowedBookEntity, bookEntity);
        decreaseUserBorrowedBooksQuantityAndSaveInRepository(userEntity);
    }

    private void returnBorrowedBook(BorrowedBookEntity borrowedBookEntity, BookEntity bookEntity) {
        borrowedBookEntity.setClosed(true);
        borrowedBookRepository.save(borrowedBookEntity);
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, borrowedBookEntity.getAffiliateEntity().getName(), 1);
        bookRepository.save(bookEntity);
        log.info("The borrowed book with id " + borrowedBookEntity.getId() + " has been returned and set as closed " +
                "in the repository");
    }

    private void decreaseUserBorrowedBooksQuantityAndSaveInRepository(UserEntity userEntity) {
        userEntity.setBorrowedBooks(userEntity.getBorrowedBooks() - 1);
        userRepository.save(userEntity);
    }

}
