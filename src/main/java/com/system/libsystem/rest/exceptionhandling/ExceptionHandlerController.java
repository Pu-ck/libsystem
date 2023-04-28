package com.system.libsystem.rest.exceptionhandling;

import com.system.libsystem.exceptions.CardNumberAlreadyTakenException;
import com.system.libsystem.exceptions.UsernameAlreadyTakenException;
import com.system.libsystem.rest.exceptionhandling.errorresponses.CardNumberAlreadyTakenResponse;
import com.system.libsystem.rest.exceptionhandling.errorresponses.UsernameAlreadyTakenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<UsernameAlreadyTakenResponse> handleUsernameAlreadyTakenException() {

        final UsernameAlreadyTakenResponse usernameAlreadyTakenResponse =
                new UsernameAlreadyTakenResponse();

        return new ResponseEntity<>(usernameAlreadyTakenResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CardNumberAlreadyTakenException.class)
    public ResponseEntity<CardNumberAlreadyTakenResponse> handleCardNumberAlreadyTakenException() {

        final CardNumberAlreadyTakenResponse cardNumberAlreadyTakenResponse =
                new CardNumberAlreadyTakenResponse();

        return new ResponseEntity<>(cardNumberAlreadyTakenResponse, HttpStatus.CONFLICT);
    }

}
