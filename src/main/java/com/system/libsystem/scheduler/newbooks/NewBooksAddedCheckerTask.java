package com.system.libsystem.scheduler.newbooks;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.newbook.NewBookEntity;
import com.system.libsystem.entities.newbook.NewBookRepository;
import com.system.libsystem.exceptions.newbook.NewBookNotFoundException;
import com.system.libsystem.scheduler.SchedulerDateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class NewBooksAddedCheckerTask {

    private static final int MAX_NEW_BOOKS_QUANTITY = 5;

    private final BookRepository bookRepository;
    private final NewBookRepository newBookRepository;

    public void updateNewAddedBooks() {
        final HashMap<Long, Long> updatedNewBooksAssociatedWithBooksMap = new HashMap<>();
        if (isNewBookEntityRepositoryEmpty()) {
            for (BookEntity bookEntity : getNewestBooks()) {
                addNewBook(bookEntity, updatedNewBooksAssociatedWithBooksMap);
            }
        } else {
            for (BookEntity bookEntity : getNewestBooks()) {
                if (!isBookAlreadyAdded(bookEntity)) {
                    removeOldBookIfNeccessary();
                    addNewBook(bookEntity, updatedNewBooksAssociatedWithBooksMap);
                }
            }
        }
        logUpdatedNewBooksIfPresent(updatedNewBooksAssociatedWithBooksMap);
    }

    private List<BookEntity> getNewestBooks() {
        final Date currentDate = SchedulerDateUtil.getCurrentDate();
        final HashMap<BookEntity, Long> bookEntityDaysInLibraryMap = new HashMap<>();
        setBooksDaysInLibrary(bookEntityDaysInLibraryMap, currentDate);
        return bookEntityDaysInLibraryMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(MAX_NEW_BOOKS_QUANTITY)
                .map(Map.Entry::getKey)
                .toList();
    }

    private void setBooksDaysInLibrary(HashMap<BookEntity, Long> bookEntityDaysInLibraryMap, Date currentDate) {
        for (BookEntity bookEntity : bookRepository.findAll()) {
            final long daysInLibrary = SchedulerDateUtil.getDaysBetweenTwoDates(currentDate, bookEntity.getAddDate());
            bookEntityDaysInLibraryMap.put(bookEntity, daysInLibrary);
        }
    }

    private void addNewBook(BookEntity bookEntity, HashMap<Long, Long> updatedNewBooksAssociatedWithBooksMap) {
        final NewBookEntity newBookEntity = new NewBookEntity();
        newBookEntity.setBookId(bookEntity.getId());
        newBookRepository.save(newBookEntity);
        updatedNewBooksAssociatedWithBooksMap.put(newBookEntity.getBookId(), bookEntity.getId());
    }

    private boolean isNewBookEntityRepositoryEmpty() {
        return newBookRepository.findAll().isEmpty();
    }

    private boolean isBookAlreadyAdded(BookEntity bookEntity) {
        for (NewBookEntity newBookEntity : newBookRepository.findAll()) {
            if (Objects.equals(newBookEntity.getBookId(), bookEntity.getId())) {
                return true;
            }
        }
        return false;
    }

    private void removeOldBookIfNeccessary() {
        final List<Long> currentNewBooksIds = newBookRepository.findAll().stream().map(NewBookEntity::getBookId).toList();
        final List<Long> newBooksIds = getNewestBooks().stream().map(BookEntity::getId).toList();
        for (Long bookId : currentNewBooksIds) {
            if (!newBooksIds.contains(bookId)) {
                final NewBookEntity newBookEntity = newBookRepository.findByBookId(bookId).orElseThrow(() ->
                        new NewBookNotFoundException(bookId));
                newBookRepository.delete(newBookEntity);
                break;
            }
        }
    }

    private void logUpdatedNewBooksIfPresent(HashMap<Long, Long> updatedNewBooksAssociatedWithBooksMap) {
        if (!updatedNewBooksAssociatedWithBooksMap.isEmpty()) {
            log.info("New books have been updated");
            log.info("New book ids and associated book ids: " + updatedNewBooksAssociatedWithBooksMap);
        }
    }

}
