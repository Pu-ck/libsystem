package com.system.libsystem.rest.administration.books;

import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.book.BorrowedBookNotFoundException;
import com.system.libsystem.rest.administration.CommonAdminPanelEntitySearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class BorrowedBooksService extends CommonAdminPanelEntitySearch<BorrowedBookRepository, BorrowedBookEntity> {

    private final UserService userService;
    private final BorrowedBookRepository borrowedBookRepository;

    BorrowedBooksService(BorrowedBookRepository borrowedBookRepository, UserService userService) {
        super(userService, borrowedBookRepository);
        this.userService = userService;
        this.borrowedBookRepository = borrowedBookRepository;
    }

    public List<BorrowedBookEntity> getBorrowedBooks(HttpServletRequest httpServletRequest) {
        return getAdminPanelEntities(httpServletRequest);
    }

    public List<BorrowedBookEntity> getBorrowedBooksById(Long borrowedBookId, HttpServletRequest httpServletRequest) {
        return getAdminPanelEntityById(borrowedBookId, httpServletRequest, new BorrowedBookNotFoundException(borrowedBookId, null, null));
    }

    public List<BorrowedBookEntity> getBorrowedBooksByCardNumber(Long cardNumber, HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        if (cardNumber == null) {
            return getAdminPanelEntities(httpServletRequest);
        }
        return borrowedBookRepository.findByCardNumber(cardNumber);
    }

    public List<BorrowedBookEntity> getBorrowedBooksByUserId(Long userId, HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        if (userId == null) {
            return getAdminPanelEntities(httpServletRequest);
        }
        return borrowedBookRepository.findByUserId(userId);
    }

    public List<BorrowedBookEntity> getBorrowedBooksByBookId(Long bookId, HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        if (bookId == null) {
            return getAdminPanelEntities(httpServletRequest);
        }
        return borrowedBookRepository.findByBookId(bookId);
    }

}
