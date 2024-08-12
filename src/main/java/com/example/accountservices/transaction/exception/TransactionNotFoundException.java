package com.example.accountservices.transaction.exception;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(UUID transactionId) {
        super(String.format("Transaction with transactionId=%s not found.", transactionId));
    }
}
