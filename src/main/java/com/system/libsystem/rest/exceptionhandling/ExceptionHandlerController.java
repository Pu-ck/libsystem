package com.system.libsystem.rest.exceptionhandling;

import com.system.libsystem.exceptions.administration.AdministratorChangesItsOwnEnabledStatusException;
import com.system.libsystem.exceptions.book.*;
import com.system.libsystem.exceptions.borrow.BookOutOfStockException;
import com.system.libsystem.exceptions.borrow.TooManyBorrowedOrOrderedBooksException;
import com.system.libsystem.exceptions.cardnumber.CardNumberAlreadyTakenException;
import com.system.libsystem.exceptions.cardnumber.CardNumberNotFoundException;
import com.system.libsystem.exceptions.cardnumber.InvalidCardNumberFormatException;
import com.system.libsystem.exceptions.cardnumber.UnableToAuthenticateCardNumberException;
import com.system.libsystem.exceptions.favourite.BookNotFoundInUserFavouriteBooksException;
import com.system.libsystem.exceptions.passwordreminder.PasswordReminderTokenExpiredException;
import com.system.libsystem.exceptions.passwordreminder.PasswordReminderTokenNotFoundException;
import com.system.libsystem.exceptions.passwordreset.NewPasswordDuplicatedException;
import com.system.libsystem.exceptions.passwordreset.OldPasswordNotMatchingException;
import com.system.libsystem.exceptions.peselnumber.InvalidPeselNumberFormatException;
import com.system.libsystem.exceptions.peselnumber.PeselNumberAlreadyTakenException;
import com.system.libsystem.exceptions.peselnumber.UnableToAuthenticatePeselNumberException;
import com.system.libsystem.exceptions.registration.UsernameAlreadyTakenException;
import com.system.libsystem.exceptions.user.UserNotEnabledException;
import com.system.libsystem.exceptions.user.UserNotFoundException;
import com.system.libsystem.rest.exceptionhandling.errorresponses.administration.AdministratorChangesItsOwnEnabledStatusResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.book.*;
import com.system.libsystem.rest.exceptionhandling.errorresponses.borrow.BookOutOfStockResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.borrow.TooManyBorrowedBooksResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber.CardNumberAlreadyTakenResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber.CardNumberNotFoundResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber.InvalidCardNumberFormatResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.cardnumber.UnableToAuthenticateCardNumberResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.favourite.BookNotFoundInUserFavouriteBooksResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.password.NewPasswordDuplicatedResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.password.OldPasswordNotMatchingResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.password.PasswordReminderTokenExpiredResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.password.PasswordReminderTokenNotFoundResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.peselnumber.InvalidPeselNumberFormatResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.peselnumber.PeselNumberAlreadyTakenResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.peselnumber.UnableToAuthenticatePeselNumberResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.registration.UsernameAlreadyTakenResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.user.UserNotEnabledResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.user.UserNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<UsernameAlreadyTakenResponse> handleUsernameAlreadyTakenException() {
        final UsernameAlreadyTakenResponse usernameAlreadyTakenResponse = new UsernameAlreadyTakenResponse();
        return new ResponseEntity<>(usernameAlreadyTakenResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CardNumberAlreadyTakenException.class)
    public ResponseEntity<CardNumberAlreadyTakenResponse> handleCardNumberAlreadyTakenException() {
        final CardNumberAlreadyTakenResponse cardNumberAlreadyTakenResponse = new CardNumberAlreadyTakenResponse();
        return new ResponseEntity<>(cardNumberAlreadyTakenResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NewPasswordDuplicatedException.class)
    public ResponseEntity<NewPasswordDuplicatedResponse> handleNewPasswordDuplicatedException() {
        final NewPasswordDuplicatedResponse newPasswordDuplicatedResponse = new NewPasswordDuplicatedResponse();
        return new ResponseEntity<>(newPasswordDuplicatedResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OldPasswordNotMatchingException.class)
    public ResponseEntity<OldPasswordNotMatchingResponse> handleOldPasswordNotMatchingException() {
        final OldPasswordNotMatchingResponse oldPasswordNotMatchingResponse = new OldPasswordNotMatchingResponse();
        return new ResponseEntity<>(oldPasswordNotMatchingResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnableToAuthenticateCardNumberException.class)
    public ResponseEntity<UnableToAuthenticateCardNumberResponse> handleUnableToAuthenticateCardNumberException() {
        final UnableToAuthenticateCardNumberResponse unableToAuthenticateCardNumberResponse
                = new UnableToAuthenticateCardNumberResponse();
        return new ResponseEntity<>(unableToAuthenticateCardNumberResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordReminderTokenNotFoundException.class)
    public ResponseEntity<PasswordReminderTokenNotFoundResponse> handlePasswordReminderTokenNotFoundException() {
        final PasswordReminderTokenNotFoundResponse passwordReminderTokenNotFoundResponse
                = new PasswordReminderTokenNotFoundResponse();
        return new ResponseEntity<>(passwordReminderTokenNotFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordReminderTokenExpiredException.class)
    public ResponseEntity<PasswordReminderTokenExpiredResponse> handlePasswordReminderTokenExpiredException() {
        final PasswordReminderTokenExpiredResponse passwordReminderTokenExpiredResponse
                = new PasswordReminderTokenExpiredResponse();
        return new ResponseEntity<>(passwordReminderTokenExpiredResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookNotFoundResponse> handleBookNotFoundException() {
        final BookNotFoundResponse bookNotFoundResponse = new BookNotFoundResponse();
        return new ResponseEntity<>(bookNotFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardNumberNotFoundException.class)
    public ResponseEntity<CardNumberNotFoundResponse> handleCardNumberNotFoundException() {
        final CardNumberNotFoundResponse cardNumberNotFoundResponse = new CardNumberNotFoundResponse();
        return new ResponseEntity<>(cardNumberNotFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnableToAuthenticatePeselNumberException.class)
    public ResponseEntity<UnableToAuthenticatePeselNumberResponse> handleUnableToAuthenticatePeselNumberException() {
        final UnableToAuthenticatePeselNumberResponse unableToAuthenticatePeselNumberResponse =
                new UnableToAuthenticatePeselNumberResponse();
        return new ResponseEntity<>(unableToAuthenticatePeselNumberResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidCardNumberFormatException.class)
    public ResponseEntity<InvalidCardNumberFormatResponse> handleInvalidCardNumberFormatException() {
        final InvalidCardNumberFormatResponse invalidCardNumberFormatResponse = new InvalidCardNumberFormatResponse();
        return new ResponseEntity<>(invalidCardNumberFormatResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPeselNumberFormatException.class)
    public ResponseEntity<InvalidPeselNumberFormatResponse> handleInvalidPeselNumberFormatException() {
        final InvalidPeselNumberFormatResponse invalidCardNumberFormatResponse = new InvalidPeselNumberFormatResponse();
        return new ResponseEntity<>(invalidCardNumberFormatResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PeselNumberAlreadyTakenException.class)
    public ResponseEntity<PeselNumberAlreadyTakenResponse> handlePeselNumberAlreadyTakenException() {
        final PeselNumberAlreadyTakenResponse peselNumberAlreadyTakenResponse = new PeselNumberAlreadyTakenResponse();
        return new ResponseEntity<>(peselNumberAlreadyTakenResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookOutOfStockException.class)
    public ResponseEntity<BookOutOfStockResponse> handleBookOutOfStockException() {
        final BookOutOfStockResponse bookOutOfStockResponse = new BookOutOfStockResponse();
        return new ResponseEntity<>(bookOutOfStockResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooManyBorrowedOrOrderedBooksException.class)
    public ResponseEntity<TooManyBorrowedBooksResponse> handleTooManyBorrowedBooksException() {
        final TooManyBorrowedBooksResponse tooManyBorrowedBooksResponse = new TooManyBorrowedBooksResponse();
        return new ResponseEntity<>(tooManyBorrowedBooksResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundInUserFavouriteBooksException.class)
    public ResponseEntity<BookNotFoundInUserFavouriteBooksResponse> handleBookNotFoundInUserFavouriteBooksException() {
        final BookNotFoundInUserFavouriteBooksResponse bookNotFoundInUserFavouriteBooksResponse
                = new BookNotFoundInUserFavouriteBooksResponse();
        return new ResponseEntity<>(bookNotFoundInUserFavouriteBooksResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundResponse> handleUserNotFoundException() {
        final UserNotFoundResponse userNotFoundResponse = new UserNotFoundResponse();
        return new ResponseEntity<>(userNotFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<UserNotEnabledResponse> handleUserNotEnabledException() {
        final UserNotEnabledResponse userNotEnabledResponse = new UserNotEnabledResponse();
        return new ResponseEntity<>(userNotEnabledResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AdministratorChangesItsOwnEnabledStatusException.class)
    public ResponseEntity<AdministratorChangesItsOwnEnabledStatusResponse> handleAdministratorChangesItsOwnEnabledStatusException() {
        final AdministratorChangesItsOwnEnabledStatusResponse administratorChangesItsOwnEnabledStatusResponse =
                new AdministratorChangesItsOwnEnabledStatusResponse();
        return new ResponseEntity<>(administratorChangesItsOwnEnabledStatusResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BorrowedBookNotFoundException.class)
    public ResponseEntity<BorrowedBookNotFoundResponse> handleBorrowedBookNotFoundException() {
        final BorrowedBookNotFoundResponse borrowedBookNotFoundResponse = new BorrowedBookNotFoundResponse();
        return new ResponseEntity<>(borrowedBookNotFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyReturnedException.class)
    public ResponseEntity<BookAlreadyReturnedResponse> handleBookAlreadyReturnedException() {
        final BookAlreadyReturnedResponse bookAlreadyReturnedResponse = new BookAlreadyReturnedResponse();
        return new ResponseEntity<>(bookAlreadyReturnedResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnableToExtendNotAcceptedBookException.class)
    public ResponseEntity<UnableToExtendNotAcceptedBookResponse> handleUnableToExtendNotAcceptedBookException() {
        final UnableToExtendNotAcceptedBookResponse unableToExtendNotAcceptedBookResponse =
                new UnableToExtendNotAcceptedBookResponse();
        return new ResponseEntity<>(unableToExtendNotAcceptedBookResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookAlreadyExtendedException.class)
    public ResponseEntity<BookAlreadyExtendedResponse> handleBookAlreadyExtendedException() {
        final BookAlreadyExtendedResponse bookAlreadyExtendedResponse =
                new BookAlreadyExtendedResponse();
        return new ResponseEntity<>(bookAlreadyExtendedResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookAlreadyAcceptedException.class)
    public ResponseEntity<BookAlreadyAcceptedResponse> handleBookAlreadyAcceptedException() {
        final BookAlreadyAcceptedResponse bookAlreadyAcceptedResponse =
                new BookAlreadyAcceptedResponse();
        return new ResponseEntity<>(bookAlreadyAcceptedResponse, HttpStatus.CONFLICT);
    }

}
