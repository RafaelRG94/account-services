package com.example.accountservices.account;

import com.example.accountservices.account.model.Account;
import jakarta.transaction.Transactional;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    private UUID accountId;
    private final String accountName = "Test Account";
    private final BigDecimal accountBalance = new BigDecimal(1234.567);
    private final Boolean treasury = true;

    private Account account;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        account = new Account(accountId, accountName, Money.of(accountBalance, "EUR"), treasury);

        accountRepository.save(account);
    }

    @Test
    void findByNameTest() {
        Optional<Account> maybeFoundAccount = accountRepository.findByName(accountName);

        assertTrue(maybeFoundAccount.isPresent());
        var accountFound = maybeFoundAccount.get();
        assertAll("accountFound",
                () -> assertEquals(accountName, accountFound.getName()),
                () -> assertEquals(accountName, accountFound.getName()),
                () -> assertEquals(accountBalance, accountFound.getAccountBalance().getNumberStripped()),
                () -> assertEquals(treasury, accountFound.getTreasury()));
    }

    @Test
    void findByName_notFoundTest() {
        Optional<Account> foundAccount = accountRepository.findByName("Non-existing Account");

        assertFalse(foundAccount.isPresent());
    }

    @Test
    void existsByNameTest() {
        Boolean exists = accountRepository.existsByName(accountName);

        assertTrue(exists);
    }

    @Test
    void existsByName_notExistsTest() {
        Boolean exists = accountRepository.existsByName("Non-Existing Account");

        assertFalse(exists);
    }
}
