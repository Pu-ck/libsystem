package com.system.libsystem.rest.administration.orderconfirm;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.rest.util.BookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

import static com.system.libsystem.util.SharedConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmOrderService {

    private static final int BORROWED_BOOK_KEEP_TIME = 1;

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookUtil bookUtil;
    private final BorrowedBookService borrowedBookService;
    private final UserService userService;
    private final BookService bookService;

    public void confirmOrder(ConfirmOrderRequest confirmOrderRequest) {

        final Date borrowDate = new Date(System.currentTimeMillis());
        final LocalDate dateMonthLater = borrowDate.toLocalDate().plusMonths(BORROWED_BOOK_KEEP_TIME);
        final Date returnDate = Date.valueOf(dateMonthLater);

        BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(confirmOrderRequest
                .getBorrowedBookId());
        final String affiliate = borrowedBookEntity.getAffiliate();
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
        final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

        if (bookUtil.isCardNumberValid(userEntity.getCardNumber(), confirmOrderRequest.getCardNumber())) {
            if (borrowedBookEntity.isAccepted()) {
                saveBorrowedBookAsConfirmed(borrowedBookEntity, borrowDate, returnDate, bookEntity, affiliate);
            } else {
                throw new IllegalStateException("Ordered book with id " + borrowedBookEntity.getId()
                        + " has been already accepted");
            }
        } else {
            throw new IllegalStateException(INVALID_CARD_NUMBER_LOG);
        }
    }

    private void saveBorrowedBookAsConfirmed(BorrowedBookEntity borrowedBookEntity, Date borrowDate, Date returnDate,
                                             BookEntity bookEntity, String affiliate) {
        borrowedBookEntity.setAccepted(true);
        borrowedBookEntity.setBorrowDate(borrowDate);
        borrowedBookEntity.setReturnDate(returnDate);
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, affiliate, -1);
        borrowedBookRepository.save(borrowedBookEntity);
        log.info("The borrowed book with id " + borrowedBookEntity.getId() + " has been set as accepted");
    }

}