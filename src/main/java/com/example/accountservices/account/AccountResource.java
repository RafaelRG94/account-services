package com.example.accountservices.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, path = "/accounts")
public class AccountResource {
    private final AccountService accountService;

    @Autowired
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }
}
