package com.example.accountservices.transaction;

import com.example.accountservices.transaction.dto.CreateTransactionRequestDto;
import com.example.accountservices.transaction.dto.TransactionDto;
import com.example.accountservices.transaction.model.CreateTransactionRequest;
import com.example.accountservices.transaction.model.Transaction;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionMapperTest {
    @Test
    void createTransactionRequestDtoToModelTest() {
        UUID senderAccountId = UUID.randomUUID();
        UUID receiverAccountId = UUID.randomUUID();
        BigDecimal transactionAmount = new BigDecimal("150.75");
        CreateTransactionRequestDto dto = new CreateTransactionRequestDto(senderAccountId, receiverAccountId, transactionAmount);

        CreateTransactionRequest createTransactionRequestDtoToModel = TransactionMapper.createTransactionRequestDtoToModel(dto);

        assertEquals(senderAccountId, createTransactionRequestDtoToModel.getSenderAccountId());
        assertEquals(receiverAccountId, createTransactionRequestDtoToModel.getReceiverAccountId());
        assertEquals(transactionAmount, createTransactionRequestDtoToModel.getTransactionAmount());
    }

    @Test
    void transactionToDtoTest() {
        UUID transactionId = UUID.randomUUID();
        UUID senderAccountId = UUID.randomUUID();
        UUID receiverAccountId = UUID.randomUUID();
        Money transactionAmount = Money.of(new BigDecimal("200.50"), "USD");
        Transaction transaction = new Transaction(transactionId, senderAccountId, receiverAccountId, transactionAmount);

        TransactionDto transactionDtoToModel = TransactionMapper.transactionToDto(transaction);

        assertEquals(transactionId, transactionDtoToModel.transactionId());
        assertEquals(senderAccountId, transactionDtoToModel.senderAccountId());
        assertEquals(receiverAccountId, transactionDtoToModel.receiverAccountId());
        assertEquals(transactionAmount.getNumberStripped(), transactionDtoToModel.transactionAmount());
        assertEquals(transactionAmount.getCurrency().getCurrencyCode(), transactionDtoToModel.senderAccountCurrency());
    }
}
