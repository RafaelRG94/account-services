package com.example.accountservices.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDto(UUID transactionId,
                             UUID senderAccountId,
                             UUID receiverAccountId,
                             BigDecimal transactionAmount,
                             String senderAccountCurrency) { }
