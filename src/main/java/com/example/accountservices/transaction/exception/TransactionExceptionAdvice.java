package com.example.accountservices.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionAdvice {
    @ExceptionHandler(TransactionBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String transactionBadRequestHandler(TransactionBadRequestException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NotEnoughAccountBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notEnoughAccountBalanceHandler(NotEnoughAccountBalanceException ex) {
        return  ex.getMessage();
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String transactionNotFoundHandler(TransactionNotFoundException ex) {
        return ex.getMessage();
    }
}
