package com.system.libsystem.rest.books.borrow;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;

import static com.system.libsystem.util.SharedConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class BorrowBookService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final SessionRegistry sessionRegistry;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;
    private final UserService userService;
    private final BookService bookService;
    private final BookUtil bookUtil;

    public void borrow(BorrowBookRequest borrowBookRequest, HttpServletRequest httpServletRequest) {

        final BigDecimal penalty = new BigDecimal("0.00");
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);

        final UserEntity userEntity = userService.getUserByUsername(username);
        BookEntity bookEntity = bookService.getBookById(borrowBookRequest.getBookId());

        final int userId = userEntity.getId();
        final Long cardNumber = userEntity.getCardNumber();
        final int bookId = bookEntity.getId();
        final int orderQuantity = borrowBookRequest.getQuantity();
        final int currentQuantity = getCurrentQuantityFromAffiliate(borrowBookRequest.getAffiliate(), bookEntity);
        final String affiliate = borrowBookRequest.getAffiliate();

        if (bookUtil.isCardNumberValid(borrowBookRequest.getCardNumber(), cardNumber)) {
            if (isOrderQuantityValid(currentQuantity, orderQuantity)) {
                for (int i = 0; i < orderQuantity; i++) {
                    BorrowedBookEntity borrowedBookEntity = new BorrowedBookEntity();
                    borrowedBookEntity.setBookId(bookId);
                    borrowedBookEntity.setUserId(userId);
                    borrowedBookEntity.setPenalty(penalty);
                    borrowedBookEntity.setCardNumber(cardNumber);
                    borrowedBookEntity.setAffiliate(affiliate);
                    borrowedBookRepository.save(borrowedBookEntity);
                    sendBookBorrowConfirmationMail(userEntity, bookEntity, borrowedBookEntity, affiliate);
                    log.info("New borrowed book order with id " + borrowedBookEntity.getId()
                            + " has been set for user with id " + userEntity.getId());
                }
            } else {
                throw new IllegalStateException("Book with id " + bookEntity.getId() + " is not available in stock " +
                        "of the library affiliate or the requested quantity of books to borrow is larger than the " +
                        "quantity in stock of the library affiliate");
            }
        } else {
            throw new IllegalStateException(INVALID_CARD_NUMBER_LOG);
        }
    }

    private int getCurrentQuantityFromAffiliate(String affiliate, BookEntity bookEntity) {
        if (Objects.equals(affiliate, AFFILIATE_A)) {
            return bookEntity.getCurrentQuantityAffiliateA();
        } else if (Objects.equals(affiliate, AFFILIATE_B)) {
            return bookEntity.getCurrentQuantityAffiliateB();
        }
        return 0;
    }

    private boolean isOrderQuantityValid(int currentQuantity, int orderQuantity) {
        return currentQuantity > 0 && orderQuantity <= currentQuantity;
    }

    private void sendBookBorrowConfirmationMail(UserEntity userEntity, BookEntity bookEntity,
                                                BorrowedBookEntity borrowedBookEntity,
                                                String affiliate) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getBookBorrowMailBody
                (userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getAuthor(),
                        borrowedBookEntity.getId(),
                        affiliate), "New book ordered");
        log.info("New sendBookBorrowConfirmationMail message sent to " + userEntity.getUsername());
    }

}
