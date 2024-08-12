package com.example.accountservices.account;

import com.example.accountservices.account.exception.AccountDataIntegrityViolationException;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceIntegrationTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Order(1)
    void findAll_emptyTest() {
        List<Account> result = accountService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void createAccount_successTest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("New Account", Money.of(new BigDecimal("200.00"), "USD"), false);

        Account savedAccount = accountService.createAccount(createAccountRequest);

        assertNotNull(savedAccount.getAccountId());
        assertEquals("New Account", savedAccount.getName());
        assertEquals(Money.of(new BigDecimal("200.00"), "USD"), savedAccount.getAccountBalance());

        Account persistedAccount = accountRepository.findById(savedAccount.getAccountId()).orElse(null);
        assertNotNull(persistedAccount);
        assertEquals(savedAccount, persistedAccount); // I didn't see this comparison in other projects where @Data annotation is used
    }

    @Test
    void findByIdOrThrow_successTest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Another Account", Money.of(new BigDecimal("153.21"), "EUR"), true);
        Account savedAccount = accountService.createAccount(createAccountRequest);

        Account foundAccount = accountService.findByIdOrThrow(savedAccount.getAccountId());

        assertNotNull(foundAccount);
        assertEquals(savedAccount, foundAccount);
    }

    @Test
    void createAccount_accountDataIntegrityViolationExceptionTest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Wrong Account", Money.of(new BigDecimal("150.00"), "USD"), false);
        accountService.createAccount(createAccountRequest);

        AccountDataIntegrityViolationException thrown = assertThrows(AccountDataIntegrityViolationException.class, () -> {
            accountService.createAccount(createAccountRequest);
        });

        assertEquals(String.format("An existing record with name=%s was already found.", createAccountRequest.getName()), thrown.getMessage());
    }
}
