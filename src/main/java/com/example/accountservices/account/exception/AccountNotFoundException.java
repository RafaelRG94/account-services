package com.example.accountservices.account.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    private static final String BY_UUID = "Account with accountId=%s not found.";
    private static final String BY_NAME = "Account with name %s not found.";

    public AccountNotFoundException(UUID accountId) {
        super(String.format(BY_UUID, accountId));
    }

    public AccountNotFoundException(String accountName) {
        super(String.format(BY_NAME, accountName));
    }
}
