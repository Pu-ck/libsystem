package com.system.libsystem.rest.administration.returnbook;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.system.libsystem.util.SharedConstants.FIND_BOOK_EXCEPTION_LOG;
import static com.system.libsystem.util.SharedConstants.FIND_USER_EXCEPTION_LOG;

@Service
@AllArgsConstructor
public class ReturnBookService {

    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final UserRepository userRepository;

    public void returnBook(ReturnBookRequest returnBookRequest) {
        BorrowedBookEntity borrowedBookEntity = borrowedBookRepository.findById(returnBookRequest.getId())
                .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG + returnBookRequest.getId()));

        BookEntity bookEntity = bookRepository.findById(borrowedBookEntity.getBookId())
                .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG + borrowedBookEntity.getBookId()));

        final UserEntity userEntity = userRepository.findById(borrowedBookEntity.getUserId())
                .orElseThrow(() ->
                        new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + borrowedBookEntity.getUserId()));

        if (Objects.equals(returnBookRequest.getCardNumber(), userEntity.getCardNumber())) {
            borrowedBookRepository.delete(borrowedBookEntity);
            bookEntity.setQuantity(bookEntity.getQuantity() + 1);
            bookRepository.save(bookEntity);
        }

    }

}
