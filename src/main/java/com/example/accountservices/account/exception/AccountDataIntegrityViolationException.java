package com.example.accountservices.account.exception;

public class AccountDataIntegrityViolationException extends RuntimeException {
    public AccountDataIntegrityViolationException(String accountName) {
        super(String.format("An existing record with name=%s was already found.", accountName));
    }
}
