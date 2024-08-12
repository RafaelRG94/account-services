package com.example.accountservices.transaction;

import com.example.accountservices.account.AccountRepository;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.transaction.exception.TransactionBadRequestException;
import com.example.accountservices.transaction.model.CreateTransactionRequest;
import com.example.accountservices.transaction.model.Transaction;
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
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    private UUID senderAccountId;
    private Account senderAccount;

    @BeforeEach
    void setUp() {
        senderAccountId = UUID.randomUUID();
        senderAccount = new Account(senderAccountId, "SenderAccount", Money.of(new BigDecimal("1000.00"), "USD"), false);

        transactionService = new TransactionService(accountRepository, transactionRepository);
    }

    @Test
    void createTransaction_transactionBadRequestExceptionTest() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(senderAccountId, senderAccountId, new BigDecimal("100.00"));
        given(accountRepository.findById(senderAccountId)).willReturn(Optional.ofNullable(senderAccount));

        TransactionBadRequestException thrown = assertThrows(TransactionBadRequestException.class, () -> transactionService.createTransaction(createTransactionRequest));
        assertEquals(thrown.getClass(), TransactionBadRequestException.class);

        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
