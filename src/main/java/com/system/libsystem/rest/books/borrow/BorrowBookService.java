package com.system.libsystem.rest.books.borrow;

import com.system.libsystem.entities.affiliate.AffiliateEntity;
import com.system.libsystem.entities.affiliate.AffiliateRepository;
import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.affiliatebook.AffiliateBookRepository;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.affiliate.AffiliateNotFoundException;
import com.system.libsystem.exceptions.borrow.BookOutOfStockException;
import com.system.libsystem.exceptions.borrow.TooManyBorrowedOrOrderedBooksException;
import com.system.libsystem.exceptions.cardnumber.UnableToAuthenticateCardNumberException;
import com.system.libsystem.exceptions.user.UserNotEnabledException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class BorrowBookService {

    private final AffiliateBookRepository affiliateBookRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final UserRepository userRepository;
    private final AffiliateRepository affiliateRepository;
    private final BookRepository bookRepository;
    private final MailSender mailSender;
    private final UserService userService;
    private final BookService bookService;
    private final BookUtil bookUtil;

    private static final int MAX_BOOKS_PER_USER = 10;

    @Transactional
    public void borrow(BorrowBookRequest borrowBookRequest, HttpServletRequest httpServletRequest) {
        final BigDecimal penalty = new BigDecimal("0.00");
        final UserEntity userEntity = userService.getCurrentlyLoggedUser(httpServletRequest);
        final BookEntity bookEntity = bookService.getBookById(borrowBookRequest.getBookId());
        final Long userId = userEntity.getId();
        final Long cardNumber = userEntity.getCardNumber();
        final Long bookId = bookEntity.getId();
        final int orderQuantity = borrowBookRequest.getQuantity();
        final int currentQuantity = getCurrentQuantityFromAffiliate(borrowBookRequest.getAffiliate(), bookEntity);
        final String affiliate = borrowBookRequest.getAffiliate();
        final AffiliateEntity affiliateEntity = affiliateRepository.findByName(affiliate).orElseThrow(() ->
                new AffiliateNotFoundException(affiliate));

        userService.validateIfUserIsEnabled(userEntity);
        if (!bookUtil.isCardNumberValid(borrowBookRequest.getCardNumber(), cardNumber)) {
            throw new UnableToAuthenticateCardNumberException();
        }
        if (!isOrderQuantityValid(currentQuantity, orderQuantity)) {
            throw new BookOutOfStockException(bookId);
        }

        if (!isOrderExceedingMaximumBooksAllowedPerUser(userEntity, orderQuantity)) {
            for (int i = 0; i < orderQuantity; i++) {
                BorrowedBookEntity borrowedBookEntity = new BorrowedBookEntity();
                borrowedBookEntity.setBookId(bookId);
                borrowedBookEntity.setUserId(userId);
                borrowedBookEntity.setPenalty(penalty);
                borrowedBookEntity.setCardNumber(cardNumber);
                borrowedBookEntity.setAffiliateEntity(affiliateEntity);
                decreaseBookQuantityAndSaveInRepository(bookEntity, affiliate);
                increaseUserOrderedBooksQuantityAndSaveInRepository(userEntity);
                borrowedBookRepository.save(borrowedBookEntity);
                sendBookBorrowConfirmationMail(userEntity, bookEntity, borrowedBookEntity, affiliate);
                log.info("New borrowed book order with id " + borrowedBookEntity.getId()
                        + " has been set for user with id " + userEntity.getId());
            }
        } else {
            throw new TooManyBorrowedOrOrderedBooksException(userId);
        }

    }

    private void decreaseBookQuantityAndSaveInRepository(BookEntity bookEntity, String affiliate) {
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, affiliate, -1);
        bookRepository.save(bookEntity);
    }

    private void increaseUserOrderedBooksQuantityAndSaveInRepository(UserEntity userEntity) {
        userEntity.setOrderedBooks(userEntity.getOrderedBooks() + 1);
        userRepository.save(userEntity);
    }

    private int getCurrentQuantityFromAffiliate(String affiliate, BookEntity bookEntity) {
        final AffiliateEntity affiliateEntity = bookEntity.getAffiliates().stream()
                .filter(searchedAffiliate -> searchedAffiliate.getName().equals(affiliate))
                .findFirst()
                .orElseThrow(() -> new AffiliateNotFoundException(affiliate));

        final AffiliateBook affiliateBook = affiliateBookRepository.findByBookIdAndAffiliateId(bookEntity.getId(),
                affiliateEntity.getId()).orElseThrow(() ->
                new AffiliateNotFoundException(affiliate));

        return affiliateBook.getCurrentQuantity();
    }

    private boolean isOrderQuantityValid(int currentQuantity, int orderQuantity) {
        return currentQuantity > 0 && orderQuantity <= currentQuantity;
    }

    private boolean isOrderExceedingMaximumBooksAllowedPerUser(UserEntity userEntity, int orderQuantity) {
        return userEntity.getOrderedBooks() + userEntity.getBorrowedBooks() + orderQuantity > MAX_BOOKS_PER_USER;
    }

    private void sendBookBorrowConfirmationMail(UserEntity userEntity, BookEntity bookEntity,
                                                BorrowedBookEntity borrowedBookEntity,
                                                String affiliate) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getBookBorrowMailBody
                (userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getFormattedAuthorsAsString(),
                        borrowedBookEntity.getId(),
                        affiliate), "New book ordered");
        log.info("New sendBookBorrowConfirmationMail message sent to " + userEntity.getUsername());
    }

}
