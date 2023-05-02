package com.system.libsystem.rest.books.filter;

import com.system.libsystem.entities.affiliate.AffiliateEntity;
import com.system.libsystem.entities.affiliate.AffiliateRepository;
import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.affiliatebook.AffiliateBookRepository;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FilterBooksService {

    private static final String SORT_BY_TITLE = "1";
    private static final String SORT_BY_AUTHOR = "2";
    private static final String SORT_BY_GENRE = "3";
    private static final String SORT_BY_PUBLISHER = "4";
    private static final String SORT_BY_YEAR_OF_PRINT = "5";
    private static final String SORT_BY_CURRENT_QUANTITY = "6";
    private static final String SORT_BY_GENERAL_QUANTITY = "7";

    private final BookRepository bookRepository;
    private final AffiliateRepository affiliateRepository;
    private final AffiliateBookRepository affiliateBookRepository;
    private final FilterBooksSortUtil filterBooksSortUtil;
    private final BookService bookService;

    public BookEntity getBookDetails(int bookId) {
        return bookService.getBookById(bookId);
    }

    public List<BookEntity> filterByBookProperties(Map<String, String> requestParameters) {
        final String title = requestParameters.get("title");
        final String author = requestParameters.get("author");
        final String genre = requestParameters.get("genre");
        final String publisher = requestParameters.get("publisher");
        final String yearOfPrint = requestParameters.get("yearOfPrint");
        final String affiliate = requestParameters.get("affiliate");
        final String sortType = requestParameters.get("sortType");
        final String sortDirection = requestParameters.get("sortDirection");
        List<BookEntity> bookEntities = new ArrayList<>();

        if (title.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByTitle(title).stream()).toList();
        }
        if (author.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByAuthorName(author).stream()).toList();
        }
        if (genre.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByGenreName(genre).stream()).toList();
        }
        if (publisher.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByPublisherName(publisher).stream()).toList();
        }
        if (yearOfPrint.length() > 0) {
            bookEntities = Stream.concat(bookEntities.stream(),
                    bookRepository.findByYearOfPrint(Integer.parseInt(yearOfPrint)).stream()).toList();
        }
        if (affiliate.length() > 0) {
            bookEntities = getBooksFilteredByAffiliate(affiliate);
        }
        if (bookEntities.isEmpty()) {
            bookEntities = bookRepository.findAll();
        }

        return getSortedBooks(sortType, sortDirection, bookEntities.stream().distinct().toList());
    }

    private List<BookEntity> getBooksFilteredByAffiliate(String affiliate) {

        final List<AffiliateEntity> affiliateEntities = affiliateRepository.findAllByName(affiliate);
        final List<Integer> affiliateEntitiesIds = affiliateEntities.stream().map(AffiliateEntity::getId).toList();
        final List<AffiliateBook> affiliateBooks = affiliateBookRepository.findByAffiliateIdIn(affiliateEntitiesIds);

        return affiliateBooks.stream().filter(affiliateBook -> affiliateBook.getCurrentQuantity() > 0)
                        .map(affiliateBook -> bookRepository.findById(affiliateBook.getBookId())
                        .orElseThrow(() -> new BookNotFoundException(affiliateBook.getBookId())))
                        .toList();
    }

    private List<BookEntity> getSortedBooks(String sortType, String sortDirection, List<BookEntity> bookEntities) {
        return switch (sortType) {
            case SORT_BY_TITLE -> filterBooksSortUtil.getBooksFilteredByTitleSorted(sortDirection, bookEntities);
            case SORT_BY_AUTHOR -> filterBooksSortUtil.getBooksFilteredByAuthorSorted(sortDirection, bookEntities);
            case SORT_BY_GENRE -> filterBooksSortUtil.getBooksFilteredByGenreSorted(sortDirection, bookEntities);
            case SORT_BY_PUBLISHER ->
                    filterBooksSortUtil.getBooksFilteredByPublisherSorted(sortDirection, bookEntities);
            case SORT_BY_YEAR_OF_PRINT ->
                    filterBooksSortUtil.getBooksFilteredByYearOfPrintSorted(sortDirection, bookEntities);
            case SORT_BY_CURRENT_QUANTITY -> filterBooksSortUtil.getBooksFilteredByCurrentQuantity(sortDirection);
            case SORT_BY_GENERAL_QUANTITY -> filterBooksSortUtil.getBooksFilteredByGeneralQuantity(sortDirection);
            default -> bookEntities;
        };
    }

}
