package com.example.accountservices.account;

import com.example.accountservices.account.dto.AccountDto;
import com.example.accountservices.account.dto.CreateAccountRequestDto;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountMapperTest {
    @Test
    void createAccountRequestDtoToModelTest() {
        var accountBalance = new BigDecimal("100.50");
        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto("TestAccount", "USD", accountBalance, true);

        CreateAccountRequest createAccountRequestDtoToModel = AccountMapper.createAccountRequestDtoToModel(createAccountRequestDto);

        assertEquals("TestAccount", createAccountRequestDtoToModel.getName());
        assertEquals(Money.of(accountBalance, "USD"), createAccountRequestDtoToModel.getAccountBalance());
        assertTrue(createAccountRequestDtoToModel.getTreasury());
    }

    @Test
    void accountToDtoTest() {
        UUID accountId = UUID.randomUUID();
        Money balance = Money.of(new BigDecimal("250.75"), "EUR");
        Account account = new Account(accountId, "TestAccount", balance, false);

        AccountDto accountDtoToModel = AccountMapper.accountToDto(account);

        assertEquals(accountId, accountDtoToModel.accountId());
        assertEquals("TestAccount", accountDtoToModel.name());
        assertEquals("EUR", accountDtoToModel.currency());
        assertEquals(balance.getNumberStripped(), accountDtoToModel.balance());

    }
}
