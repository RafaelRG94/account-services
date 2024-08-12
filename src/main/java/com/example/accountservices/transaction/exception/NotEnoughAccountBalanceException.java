package com.example.accountservices.transaction.exception;

import org.javamoney.moneta.Money;

import java.util.UUID;

public class NotEnoughAccountBalanceException extends RuntimeException {
    public NotEnoughAccountBalanceException(UUID accountId, Money transactionAmount) {
        super(String.format("Account with accountId=%s has insufficient available balance to create transaction with amount=%s.", accountId, transactionAmount.getNumberStripped()));
    }
}
