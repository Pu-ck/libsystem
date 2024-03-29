package com.system.libsystem.rest.administration.books;

import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.exceptions.book.BorrowedBookNotFoundException;
import com.system.libsystem.rest.administration.CommonAdminPanelEntitySearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BorrowedBooksServiceImpl extends CommonAdminPanelEntitySearch<BorrowedBookRepository, BorrowedBookEntity>
        implements BorrowedBooksService {

    private final BorrowedBookRepository borrowedBookRepository;

    BorrowedBooksServiceImpl(BorrowedBookRepository borrowedBookRepository) {
        super(borrowedBookRepository);
        this.borrowedBookRepository = borrowedBookRepository;
    }

    @Override
    public List<BorrowedBookEntity> getBorrowedBooks() {
        return getAdminPanelEntities();
    }

    @Override
    public List<BorrowedBookEntity> getBorrowedBooksById(Long borrowedBookId) {
        return getAdminPanelEntityById(borrowedBookId, new BorrowedBookNotFoundException(borrowedBookId, null, null));
    }

    @Override
    public List<BorrowedBookEntity> getBorrowedBooksByCardNumber(Long cardNumber) {
        if (cardNumber == null) {
            return getAdminPanelEntities();
        }
        return borrowedBookRepository.findByCardNumber(cardNumber);
    }

    @Override
    public List<BorrowedBookEntity> getBorrowedBooksByUserId(Long userId) {
        if (userId == null) {
            return getAdminPanelEntities();
        }
        return borrowedBookRepository.findByUserId(userId);
    }

    @Override
    public List<BorrowedBookEntity> getBorrowedBooksByBookId(Long bookId) {
        if (bookId == null) {
            return getAdminPanelEntities();
        }
        return borrowedBookRepository.findByBookId(bookId);
    }

}
