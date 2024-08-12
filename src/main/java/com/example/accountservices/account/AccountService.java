package com.example.accountservices.account;

import com.example.accountservices.account.exception.AccountBadRequestException;
import com.example.accountservices.account.exception.AccountDataIntegrityViolationException;
import com.example.accountservices.account.exception.AccountNotFoundException;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByIdOrThrow(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public Account findByNameOrThrow(String accountName) {
        return accountRepository.findByName(accountName)
                .orElseThrow(() -> new AccountNotFoundException(accountName));
    }

    public Account createAccount(CreateAccountRequest createAccountRequest) {
        String createAccountRequestName = createAccountRequest.getName();
        if (!createAccountRequest.getTreasury() && createAccountRequest.getAccountBalance().isNegative()) {
            throw new AccountBadRequestException(); // I don't know if I should use a more generic message
        } else if (accountRepository.existsByName(createAccountRequestName)) {
            throw new AccountDataIntegrityViolationException(createAccountRequestName);
        } else {
            Account account = createAccountRequestToNewAccount(createAccountRequest);
            return accountRepository.save(account); // Try to add an exception handler?
        }
    }

    private Account createAccountRequestToNewAccount(CreateAccountRequest createAccountRequest) {
        return new Account(UUID.randomUUID(),
                createAccountRequest.getName(),
                createAccountRequest.getAccountBalance(),
                createAccountRequest.getTreasury());
    }
}
