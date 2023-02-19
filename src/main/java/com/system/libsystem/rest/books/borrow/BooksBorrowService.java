package com.system.libsystem.rest.books.borrow;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

import static com.system.libsystem.util.SharedConstants.USER_EXCEPTION_LOG;

@Service
@AllArgsConstructor
public class BooksBorrowService {

    private final BookRepository bookRepository;
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final BorrowedBookRepository borrowedBookRepository;

    public void borrow(BooksBorrowRequest booksBorrowRequest, HttpServletRequest httpServletRequest) {

        final Date borrowDate = new Date(System.currentTimeMillis());
        final LocalDate dateMonthLater = borrowDate.toLocalDate().plusMonths(1);
        final Date returnDate = Date.valueOf(dateMonthLater);

        final BigDecimal penalty = new BigDecimal("0.00");
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG + username));
        BookEntity bookEntity = bookRepository.findById(booksBorrowRequest.getBookId())
                .orElseThrow(() -> new IllegalStateException("Unable to find requested book with id "
                        + booksBorrowRequest.getBookId()));

        final int userId = userEntity.getId();
        final Long cardNumber = userEntity.getCardNumber();
        final int bookId = bookEntity.getId();

        if (Objects.equals(booksBorrowRequest.getCardNumber(), cardNumber)) {
            if (bookEntity.getQuantity() > 0) {
                for (BorrowedBookEntity borrowedBookEntity : borrowedBookRepository.findByUserId(userEntity.getId())) {
                    if (borrowedBookEntity.getBookId() == bookId) {
                        throw new IllegalStateException("Book with id " + bookId + " is already borrowed by user "
                                + username);
                    } else {
                        saveBorrowedBookToRepository(bookId, userId, borrowDate, returnDate, penalty, bookEntity);
                    }
                }
            } else {
                throw new IllegalStateException("Book with id " + bookEntity.getId() + " is not available in stock");
            }

        } else {
            throw new IllegalStateException("Invalid card number");
        }
    }

    private void saveBorrowedBookToRepository(int bookId, int userId, Date borrowDate, Date returnDate,
                                              BigDecimal penalty, BookEntity bookEntity) {
        BorrowedBookEntity borrowedBookEntity = new BorrowedBookEntity();
        borrowedBookEntity.setBookId(bookId);
        borrowedBookEntity.setUserId(userId);
        borrowedBookEntity.setBorrowDate(borrowDate);
        borrowedBookEntity.setReturnDate(returnDate);
        borrowedBookEntity.setPenalty(penalty);
        borrowedBookRepository.save(borrowedBookEntity);
        bookEntity.setQuantity(bookEntity.getQuantity() - 1);
        bookRepository.save(bookEntity);
    }

}
