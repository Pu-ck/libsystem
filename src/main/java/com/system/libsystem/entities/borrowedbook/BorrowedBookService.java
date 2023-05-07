package com.system.libsystem.entities.borrowedbook;

import com.system.libsystem.exceptions.book.BorrowedBookNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BorrowedBookService {

    private final BorrowedBookRepository borrowedBookRepository;

    public BorrowedBookEntity getBorrowedBookById(int borrowedBookId) throws BorrowedBookNotFoundException {
        return borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new BorrowedBookNotFoundException(borrowedBookId));
    }

}
