package com.system.libsystem.rest.administration.orders.orderaccept;

import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.book.BookAlreadyAcceptedException;
import com.system.libsystem.exceptions.book.BookAlreadyReturnedException;
import com.system.libsystem.exceptions.cardnumber.InvalidCardNumberFormatException;
import com.system.libsystem.rest.util.BookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AcceptOrderService {

    private static final int BORROWED_BOOK_KEEP_TIME = 1;

    private final BorrowedBookRepository borrowedBookRepository;
    private final UserRepository userRepository;
    private final BookUtil bookUtil;
    private final BorrowedBookService borrowedBookService;
    private final UserService userService;

    public void confirmOrder(AcceptOrderRequest acceptOrderRequest) {

        final Date borrowDate = new Date(System.currentTimeMillis());
        final LocalDate dateMonthLater = borrowDate.toLocalDate().plusMonths(BORROWED_BOOK_KEEP_TIME);
        final Date returnDate = Date.valueOf(dateMonthLater);

        BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(acceptOrderRequest
                .getBorrowedBookId());
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());

        if (bookUtil.isCardNumberValid(userEntity.getCardNumber(), acceptOrderRequest.getCardNumber())) {
            if (!borrowedBookEntity.isClosed()) {
                if (!borrowedBookEntity.isAccepted()) {
                    updateUserOrderedAndBorrowedBooksQuantity(userEntity);
                    saveBorrowedBookAsConfirmed(borrowedBookEntity, borrowDate, returnDate);
                } else {
                    throw new BookAlreadyAcceptedException(borrowedBookEntity.getId());
                }
            } else {
                throw new BookAlreadyReturnedException(borrowedBookEntity.getId());
            }
        } else {
            throw new InvalidCardNumberFormatException();
        }
    }

    private void saveBorrowedBookAsConfirmed(BorrowedBookEntity borrowedBookEntity, Date borrowDate, Date returnDate) {
        borrowedBookEntity.setAccepted(true);
        borrowedBookEntity.setBorrowDate(borrowDate);
        borrowedBookEntity.setReturnDate(returnDate);
        borrowedBookRepository.save(borrowedBookEntity);
        log.info("The borrowed book with id " + borrowedBookEntity.getId() + " has been set as accepted");
    }

    private void updateUserOrderedAndBorrowedBooksQuantity(UserEntity userEntity) {
        userEntity.setOrderedBooks(userEntity.getOrderedBooks() - 1);
        userEntity.setBorrowedBooks(userEntity.getBorrowedBooks() + 1);
        userRepository.save(userEntity);
    }

}