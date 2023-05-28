package com.system.libsystem.rest.user;

import com.system.libsystem.entities.affiliate.AffiliateEntity;
import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.author.AuthorEntity;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.genre.GenreEntity;
import com.system.libsystem.entities.publisher.PublisherEntity;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.entities.yearofprint.YearOfPrintEntity;
import com.system.libsystem.helpermodels.UserBook;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.session.SessionRegistry;
import com.system.libsystem.util.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserProfileServiceTest {

    @Mock
    SessionRegistry sessionRegistry;
    @Mock
    UserService userService;
    @Mock
    BorrowedBookRepository borrowedBookRepository;
    @Mock
    BookService bookService;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    MailSender mailSender;
    @Mock
    BorrowedBookService borrowedBookService;

    @InjectMocks
    UserProfileService userProfileService;

    @BeforeEach
    void commonSetupForTests() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPassword("password");
        userEntity.setUsername("username");
        userEntity.setUserType(UserType.USER);
        userEntity.setBorrowedBooks(0);
        userEntity.setOrderedBooks(0);
        userEntity.setEnabled(true);
        userEntity.setCardNumber(1123456789L);
        userEntity.setFirstName("firstName");
        userEntity.setLastName("lastName");

        when(sessionRegistry.getSessionUsername(anyString())).thenReturn("username");
        when(userService.getUserByUsername(anyString())).thenReturn(userEntity);
    }

    @BeforeEach
    void setupForGetUserBooksTest() {
        final PublisherEntity publisherEntity = getDummyPublisherEntity();
        final YearOfPrintEntity yearOfPrintEntity = getDummyYearOfPrintEntity();

        final AffiliateBook affiliateBook = getDummyAffiliateBook();
        List<AffiliateBook> affiliateBooks = new ArrayList<>();
        affiliateBooks.add(affiliateBook);

        Set<AffiliateEntity> affiliates = new HashSet<>();
        AffiliateEntity affiliateEntity = getDummyAffiliateEntity();

        final Set<AuthorEntity> authors = new HashSet<>();
        AuthorEntity authorEntity = getDummyAuthorEntity();

        final Set<GenreEntity> genres = new HashSet<>();
        GenreEntity genreEntity = getDummyGenreEntity();

        final BookEntity bookEntity = getDummyBookEntity(publisherEntity, affiliates, affiliateBooks, authors, genres, yearOfPrintEntity);

        Set<BookEntity> bookEntities = new HashSet<>();
        bookEntities.add(bookEntity);
        affiliateEntity.setBooks(bookEntities);
        affiliates.add(affiliateEntity);
        authorEntity.setBooks(bookEntities);
        genreEntity.setBooks(bookEntities);

        final List<BorrowedBookEntity> borrowedBookEntities = new ArrayList<>();
        final BorrowedBookEntity borrowedBookEntity = getDummyBorrowedBookEntity(affiliateEntity);
        borrowedBookEntities.add(borrowedBookEntity);

        when(borrowedBookRepository.findByUserId(anyLong())).thenReturn(borrowedBookEntities);
        when(bookService.getBookById(anyLong())).thenReturn(bookEntity);
    }

    @Test
    void getUserProfileInformation_WithValidUser_ReturnsListOfUserProfileInformation() {
        List<String> userProfileInformation = userProfileService.getUserProfileInformation(getMockedHttpServletRequest());

        assertNotNull(userProfileInformation);
        assertEquals(6, userProfileInformation.size());
        assertEquals("username", userProfileInformation.get(0));
        assertEquals("firstName", userProfileInformation.get(1));
        assertEquals("lastName", userProfileInformation.get(2));
        assertEquals("1123456789", userProfileInformation.get(3));
        assertEquals("0", userProfileInformation.get(4));
        assertEquals("0", userProfileInformation.get(5));
    }

    @Test
    void getUserBooks_WithValidUser_ReturnsListOfUserBooks() {
        List<UserBook> userBooks = userProfileService.getUserBooks(getMockedHttpServletRequest());

        assertNotNull(userBooks);
        assertEquals(1, userBooks.size());
        assertEquals("Title 1", userBooks.get(0).getTitle());
        assertEquals(1L, userBooks.get(0).getBorrowedBookId());
        assertEquals("/books/1", userBooks.get(0).getBookDetailsLink());
        assertEquals("2023-05-26", userBooks.get(0).getBorrowDate());
        assertNull(userBooks.get(0).getReadyDate());
        assertEquals("2023-05-26", userBooks.get(0).getReturnDate());
        assertEquals("0.0", userBooks.get(0).getPenalty());
        assertEquals("Affiliate 1", userBooks.get(0).getAffiliate());
        assertEquals("Borrowed", userBooks.get(0).getStatus());
        assertFalse(userBooks.get(0).isExtended());
        assertTrue(userBooks.get(0).isAccepted());
        assertFalse(userBooks.get(0).isClosed());
    }

    @Test
    void changeUserPassword_WithValidUser_SetsNewPasswordForUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");

        when(bCryptPasswordEncoder.matches("password", "password")).thenReturn(true);
        when(bCryptPasswordEncoder.matches("password", "newPassword")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("newPassword");

        when(userService.getUserByUsername("username")).thenReturn(userEntity);
        userProfileService.changeUserPassword(changePasswordRequest, getMockedHttpServletRequest());

        assertEquals("newPassword", userEntity.getPassword());
    }

    @Test
    void extendBookReturnDate_WithValidBorrowedBook_SetsBorrowedBookExtendedStatusToTrue() {
        BorrowedBookEntity borrowedBookEntity = new BorrowedBookEntity();
        borrowedBookEntity.setId(1L);
        borrowedBookEntity.setExtended(false);
        borrowedBookEntity.setClosed(false);
        borrowedBookEntity.setBorrowDate(new Date(System.currentTimeMillis()));
        borrowedBookEntity.setReturnDate(new Date(System.currentTimeMillis()));
        borrowedBookEntity.setAffiliateEntity(new AffiliateEntity());
        borrowedBookEntity.setPenalty(BigDecimal.valueOf(0.00));

        ExtendBookRequest extendBookRequest = new ExtendBookRequest(1L, 11234567890L);
        when(borrowedBookService.getBorrowedBookById(1L)).thenReturn(borrowedBookEntity);

        when(borrowedBookService.getBorrowedBookById(1L)).thenReturn(borrowedBookEntity);
        userProfileService.extendBookReturnDate(extendBookRequest, getMockedHttpServletRequest());

        assertTrue(borrowedBookEntity.isExtended());
    }

    MockHttpServletRequest getMockedHttpServletRequest() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + anyString());
        return mockHttpServletRequest;
    }

    PublisherEntity getDummyPublisherEntity() {
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName("Publisher 1");
        publisherEntity.setId(1L);
        return publisherEntity;
    }

    YearOfPrintEntity getDummyYearOfPrintEntity() {
        YearOfPrintEntity yearOfPrintEntity = new YearOfPrintEntity();
        yearOfPrintEntity.setYearOfPrint(1999);
        yearOfPrintEntity.setId(1L);
        return yearOfPrintEntity;
    }

    AffiliateBook getDummyAffiliateBook() {
        AffiliateBook affiliateBook = new AffiliateBook();
        affiliateBook.setBookId(1L);
        affiliateBook.setAffiliateId(1L);
        affiliateBook.setCurrentQuantity(2);
        affiliateBook.setCurrentQuantity(5);
        return affiliateBook;
    }

    AffiliateEntity getDummyAffiliateEntity() {
        AffiliateEntity affiliateEntity = new AffiliateEntity();
        affiliateEntity.setPhoneNumber("123123123");
        affiliateEntity.setAddress("Address");
        affiliateEntity.setName("Affiliate 1");
        return affiliateEntity;
    }

    AuthorEntity getDummyAuthorEntity() {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName("Author 1");
        authorEntity.setId(1L);
        return authorEntity;
    }

    GenreEntity getDummyGenreEntity() {
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setName("Genre 1");
        genreEntity.setId(1L);
        return genreEntity;
    }

    BookEntity getDummyBookEntity(PublisherEntity publisherEntity, Set<AffiliateEntity> affiliates,
                                  List<AffiliateBook> affiliateBooks, Set<AuthorEntity> authors, Set<GenreEntity> genres,
                                  YearOfPrintEntity yearOfPrintEntity) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setTitle("Title 1");
        bookEntity.setPublisherEntity(publisherEntity);
        bookEntity.setAffiliates(affiliates);
        bookEntity.setAffiliateBooks(affiliateBooks);
        bookEntity.setAuthors(authors);
        bookEntity.setDescription("Description");
        bookEntity.setGenres(genres);
        bookEntity.setYearOfPrintEntity(yearOfPrintEntity);
        return bookEntity;
    }

    BorrowedBookEntity getDummyBorrowedBookEntity(AffiliateEntity affiliateEntity) {
        BorrowedBookEntity borrowedBookEntity = new BorrowedBookEntity();
        borrowedBookEntity.setId(1L);
        borrowedBookEntity.setBookId(1L);
        borrowedBookEntity.setExtended(false);
        borrowedBookEntity.setClosed(false);
        borrowedBookEntity.setAccepted(true);
        borrowedBookEntity.setReady(true);
        borrowedBookEntity.setPenalty(BigDecimal.valueOf(0.00));
        borrowedBookEntity.setAffiliateEntity(affiliateEntity);
        borrowedBookEntity.setBorrowDate(Date.valueOf("2023-05-26"));
        borrowedBookEntity.setReadyDate(Date.valueOf("2023-05-26"));
        borrowedBookEntity.setReturnDate(Date.valueOf("2023-05-26"));
        borrowedBookEntity.setCardNumber(1123456789L);
        return borrowedBookEntity;
    }

}
