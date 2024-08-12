package com.example.accountservices.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionAdvice {
    @ExceptionHandler(AccountBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String accountBadRequestHandler(AccountBadRequestException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String accountNotFoundHandler(AccountNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccountDataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String accountConflictHandler(AccountDataIntegrityViolationException ex) {
        return ex.getMessage();
    }
}
