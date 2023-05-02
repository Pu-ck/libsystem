package com.system.libsystem.rest.exceptionhandling;

import com.system.libsystem.exceptions.*;
import com.system.libsystem.rest.exceptionhandling.errorresponses.*;
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

}
