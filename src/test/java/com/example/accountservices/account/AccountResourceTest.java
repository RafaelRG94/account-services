package com.example.accountservices.account;

import com.example.accountservices.account.exception.AccountDataIntegrityViolationException;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountResource.class)
public class AccountResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void getAllAccountsTest() throws Exception {
        UUID accountId = UUID.randomUUID();
        Money balance = Money.of(new BigDecimal("123.456"), "USD");

        given(accountService.findAll())
                .willReturn(List.of(new Account(accountId, "Test Account", balance, false)));

        mockMvc.perform(get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(accountId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Account"))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].balance").value(123.456));
    }

    @Test
    void getAccountByIdTest() throws Exception {
        UUID accountId = UUID.randomUUID();
        Money balance = Money.of(new BigDecimal("543.21"), "EUR");

        given(accountService.findByIdOrThrow(accountId))
                .willReturn(new Account(accountId, "Test Account", balance, false));

        mockMvc.perform(get("/accounts/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.balance").value(543.21));
    }

    @Test
    void createAccount_successTest() throws Exception {
        UUID accountId = UUID.randomUUID();
        Money balance = Money.of(new BigDecimal("707.23"), "USD");

        given(accountService.createAccount(any(CreateAccountRequest.class)))
                .willReturn(new Account(accountId, "New Account", balance, false));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "accountName": "New Account",
                                    "currency": "USD",
                                    "accountBalance": 707.23,
                                    "treasury": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.name").value("New Account"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(707.23));
    }

    @Test
    void createAccount_accountNameExistsTest() throws Exception {
        given(accountService.createAccount(any(CreateAccountRequest.class)))
                .willThrow(new AccountDataIntegrityViolationException("New Account"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "accountName": "New Account",
                                    "currency": "USD",
                                    "accountBalance": 707.23,
                                    "treasury": false
                                }
                                """))
                .andExpect(status().isConflict());
    }
}
