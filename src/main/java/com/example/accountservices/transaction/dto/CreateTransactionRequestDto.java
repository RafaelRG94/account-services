package com.example.accountservices.transaction.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequestDto (@NotEmpty UUID senderAccountId,
                                          @NotEmpty UUID receiverAccountId,
                                          @NotNull BigDecimal transactionAmount) { }
