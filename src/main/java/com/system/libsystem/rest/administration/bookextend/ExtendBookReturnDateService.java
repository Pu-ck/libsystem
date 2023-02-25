package com.system.libsystem.rest.administration.bookextend;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

import static com.system.libsystem.util.SharedConstants.*;

@Service
@AllArgsConstructor
public class ExtendBookReturnDateService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;

    public void extendRequestedBookReturnDate(ExtendBookReturnDateRequest extendBookReturnDateRequest) {

        BorrowedBookEntity borrowedBookEntity = borrowedBookRepository
                .findById(extendBookReturnDateRequest.getBorrowedBookId())
                .orElseThrow(() -> new IllegalStateException(FIND_BORROWED_BOOK_EXCEPTION_LOG));

        final UserEntity userEntity = userRepository.findById(borrowedBookEntity.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG
                        + borrowedBookEntity.getUserId()));

        final BookEntity bookEntity = bookRepository.findById(borrowedBookEntity.getBookId())
                .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG + borrowedBookEntity.getBookId()));

        final Date currentReturnDate = borrowedBookEntity.getReturnDate();
        final LocalDate extendTime = currentReturnDate.toLocalDate().plusDays(extendBookReturnDateRequest.getExtendTime());
        final Date newReturnDate = Date.valueOf(extendTime);

        if (!borrowedBookEntity.isAccepted()) {
            borrowedBookEntity.setReturnDate(newReturnDate);
            borrowedBookEntity.setExtended(true);
            borrowedBookRepository.save(borrowedBookEntity);
            sendBookReturnDateExtensionConfirmationMail(userEntity, borrowedBookEntity, bookEntity);
        }
    }

    private void sendBookReturnDateExtensionConfirmationMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity,
                                                             BookEntity bookEntity) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getBookReturnDateExtensionConfirmationMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getAuthor(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliate()),
                "Book return date extension request accepted");
    }

}
