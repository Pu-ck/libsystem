package com.system.libsystem.rest.administration.orderconfirm;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

import static com.system.libsystem.util.SharedConstants.FIND_BOOK_EXCEPTION_LOG;
import static com.system.libsystem.util.SharedConstants.FIND_USER_EXCEPTION_LOG;

@Service
@RequiredArgsConstructor
public class ConfirmOrderService {

    private static final int BORROWED_BOOK_KEEP_TIME = 1;

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public void confirmOrder(ConfirmOrderRequest confirmOrderRequest) {

        final Date borrowDate = new Date(System.currentTimeMillis());
        final LocalDate dateMonthLater = borrowDate.toLocalDate().plusMonths(BORROWED_BOOK_KEEP_TIME);
        final Date returnDate = Date.valueOf(dateMonthLater);

        BorrowedBookEntity borrowedBookEntity = borrowedBookRepository.findById(confirmOrderRequest.getId())
                .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG + confirmOrderRequest.getId()));

        final int userId = borrowedBookEntity.getUserId();

        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG));

        BookEntity bookEntity = bookRepository.findById(borrowedBookEntity.getBookId())
                .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG + borrowedBookEntity.getBookId()));

        if (Objects.equals(userEntity.getCardNumber(), confirmOrderRequest.getCardNumber())) {
            borrowedBookEntity.setAccepted(true);
            borrowedBookEntity.setBorrowDate(borrowDate);
            borrowedBookEntity.setReturnDate(returnDate);
            bookEntity.setQuantity(bookEntity.getQuantity() - 1);
            borrowedBookRepository.save(borrowedBookEntity);
        } else {
            throw new IllegalStateException("Invalid card number");
        }
    }

}