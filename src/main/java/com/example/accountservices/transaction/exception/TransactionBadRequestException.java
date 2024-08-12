package com.example.accountservices.transaction.exception;

public class TransactionBadRequestException extends RuntimeException {
    public TransactionBadRequestException() {
        super("Origin and destiny accounts can not be the same.");
    }
}
