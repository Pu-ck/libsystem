package com.system.libsystem.rest.books.borrow;

import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.affiliatebook.AffiliateBookRepository;
import com.system.libsystem.entities.affiliate.AffiliateEntity;
import com.system.libsystem.entities.affiliate.AffiliateRepository;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.AffiliateNotFoundException;
import com.system.libsystem.exceptions.InvalidCardNumberException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    private final AffiliateRepository affiliateRepository;
    private final BookRepository bookRepository;
    private final SessionRegistry sessionRegistry;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;
    private final UserService userService;
    private final BookService bookService;
    private final BookUtil bookUtil;

    @Transactional
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
        final AffiliateEntity affiliateEntity = affiliateRepository.findByName(affiliate).orElseThrow(() ->
                new AffiliateNotFoundException(affiliate));

        if (bookUtil.isCardNumberValid(borrowBookRequest.getCardNumber(), cardNumber)) {
            if (isOrderQuantityValid(currentQuantity, orderQuantity)) {
                for (int i = 0; i < orderQuantity; i++) {
                    BorrowedBookEntity borrowedBookEntity = new BorrowedBookEntity();
                    borrowedBookEntity.setBookId(bookId);
                    borrowedBookEntity.setUserId(userId);
                    borrowedBookEntity.setPenalty(penalty);
                    borrowedBookEntity.setCardNumber(cardNumber);
                    borrowedBookEntity.setAffiliateEntity(affiliateEntity);
                    decreaseBookQuantityAndSaveItInRepository(bookEntity, affiliate);
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
            throw new InvalidCardNumberException();
        }
    }

    private void decreaseBookQuantityAndSaveItInRepository(BookEntity bookEntity, String affiliate) {
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, affiliate, -1);
        bookRepository.save(bookEntity);
    }

    private int getCurrentQuantityFromAffiliate(String affiliate, BookEntity bookEntity) {
        final AffiliateEntity affiliateEntity = bookEntity.getAffiliates().stream()
                .filter(searchedAffiliate -> searchedAffiliate.getName().equals(affiliate))
                .findFirst()
                .orElseThrow(() -> new AffiliateNotFoundException(affiliate));

        AffiliateBook affiliateBook = affiliateBookRepository.findByBookIdAndAffiliateId(bookEntity.getId(),
                affiliateEntity.getId()).orElseThrow(() ->
                new AffiliateNotFoundException(affiliate));

        return affiliateBook.getCurrentQuantity();
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
                        String.join(",", bookEntity.getAuthors().stream().toList().toString()),
                        borrowedBookEntity.getId(),
                        affiliate), "New book ordered");
        log.info("New sendBookBorrowConfirmationMail message sent to " + userEntity.getUsername());
    }

}
