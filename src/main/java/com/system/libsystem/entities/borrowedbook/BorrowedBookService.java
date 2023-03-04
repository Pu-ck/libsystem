package com.system.libsystem.entities.borrowedbook;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BorrowedBookService {

    private final BorrowedBookRepository borrowedBookRepository;

    public BorrowedBookEntity getBorrowedBookById(int borrowedBookId) throws IllegalStateException {
        return borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new IllegalStateException("Unable to find borrowed book with id " + borrowedBookId));
    }

}
