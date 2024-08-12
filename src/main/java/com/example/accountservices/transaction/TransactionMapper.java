package com.example.accountservices.transaction;

import com.example.accountservices.transaction.dto.CreateTransactionRequestDto;
import com.example.accountservices.transaction.dto.TransactionDto;
import com.example.accountservices.transaction.model.CreateTransactionRequest;
import com.example.accountservices.transaction.model.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {
    static CreateTransactionRequest createTransactionRequestDtoToModel(CreateTransactionRequestDto createTransactionRequestDto) {
        return new CreateTransactionRequest(
                createTransactionRequestDto.senderAccountId(),
                createTransactionRequestDto.receiverAccountId(),
                createTransactionRequestDto.transactionAmount()
        );
    }

    static TransactionDto transactionToDto(Transaction transaction) {
        return new TransactionDto(transaction.getTransactionId(),
                transaction.getSenderAccountId(),
                transaction.getReceiverAccountId(),
                transaction.getTransactionAmount().getNumberStripped(),
                transaction.getTransactionAmount().getCurrency().toString());
    }
}
