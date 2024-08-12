package com.example.accountservices.account.exception;

public class AccountBadRequestException extends RuntimeException{
    public AccountBadRequestException() {
        super("Account data not valid.");
    }
}
