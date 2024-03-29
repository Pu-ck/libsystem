package com.system.libsystem.rest.administration.books.orders.orderaccept;

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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AcceptOrderServiceImpl implements AcceptOrderService {

    private static final int BORROWED_BOOK_KEEP_TIME = 1;

    private final BorrowedBookRepository borrowedBookRepository;
    private final UserRepository userRepository;
    private final BorrowedBookService borrowedBookService;
    private final UserService userService;

    @Transactional
    @Override
    public void confirmOrder(AcceptOrderRequest acceptOrderRequest) {
        final Date borrowDate = new Date(System.currentTimeMillis());
        final LocalDate dateMonthLater = borrowDate.toLocalDate().plusMonths(BORROWED_BOOK_KEEP_TIME);
        final Date returnDate = Date.valueOf(dateMonthLater);
        final BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(acceptOrderRequest
                .getBorrowedBookId());
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());

        if (!BookUtil.isCardNumberValid(userEntity.getCardNumber(), acceptOrderRequest.getCardNumber())) {
            throw new InvalidCardNumberFormatException();
        }
        if (borrowedBookEntity.isClosed()) {
            throw new BookAlreadyReturnedException(borrowedBookEntity.getId());
        }
        if (borrowedBookEntity.isAccepted()) {
            throw new BookAlreadyAcceptedException(borrowedBookEntity.getId());
        }

        updateUserOrderedAndBorrowedBooksQuantity(userEntity);
        saveBorrowedBookAsConfirmed(borrowedBookEntity, borrowDate, returnDate);
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