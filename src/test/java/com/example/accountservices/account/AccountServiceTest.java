package com.example.accountservices.account;

import com.example.accountservices.account.exception.AccountBadRequestException;
import com.example.accountservices.account.exception.AccountDataIntegrityViolationException;
import com.example.accountservices.account.exception.AccountNotFoundException;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {
    @MockBean
    private AccountRepository accountRepository;
    private AccountService accountService;

    private UUID accountId;
    private String accountName;


    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountName = "Test Account";

        accountService = new AccountService(accountRepository);
    }

    @Test
    void findByIdOrThrow_accountNotFoundExceptionTest() {
        given(accountRepository.findById(accountId)).willReturn(Optional.empty()); // Is it better to use when-thenReturn?

        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class, () -> accountService.findByIdOrThrow(accountId));
        assertEquals(String.format("Account with accountId=%s not found.", accountId), thrown.getMessage());

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void findByNameOrThrow_accountNotFoundExceptionTest() {
        given(accountRepository.findByName(accountName)).willReturn(Optional.empty());

        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class, () -> accountService.findByNameOrThrow(accountName));
        assertEquals(String.format("Account with name %s not found.", accountName), thrown.getMessage());

        verify(accountRepository, times(1)).findByName(accountName);
    }

    @Test
    void createAccount_accountBadRequestExceptionTest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("NewAccount", Money.of(new BigDecimal("-50.00"), "USD"), false);

        AccountBadRequestException thrown = assertThrows(AccountBadRequestException.class, () -> accountService.createAccount(createAccountRequest));
        assertEquals(thrown.getClass(), AccountBadRequestException.class);

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void createAccount_accountDataIntegrityViolationExceptionTest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(accountName, Money.of(new BigDecimal("150.00"), "USD"), false);
        given(accountRepository.existsByName(accountName)).willReturn(true);

        AccountDataIntegrityViolationException thrown = assertThrows(AccountDataIntegrityViolationException.class, () -> accountService.createAccount(createAccountRequest));
        assertEquals(String.format("An existing record with name=%s was already found.", accountName), thrown.getMessage());

        verify(accountRepository, times(1)).existsByName(accountName);
        verify(accountRepository, never()).save(any(Account.class));
    }
}
