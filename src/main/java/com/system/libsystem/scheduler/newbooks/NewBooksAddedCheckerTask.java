package com.system.libsystem.scheduler.newbooks;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.newbook.NewBookEntity;
import com.system.libsystem.entities.newbook.NewBookRepository;
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
        for (BookEntity bookEntity : getNewestBooks()) {
            for (NewBookEntity currentNewBookEntity : newBookRepository.findAll()) {
                if (!Objects.equals(currentNewBookEntity.getBookId(), bookEntity.getId())) {
                    final NewBookEntity newBookEntity = new NewBookEntity();
                    newBookEntity.setBookId(bookEntity.getId());
                    newBookRepository.delete(currentNewBookEntity);
                    newBookRepository.save(newBookEntity);
                }
            }
        }
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

}
