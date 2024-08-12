package com.example.accountservices.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequestDto(@NotEmpty String accountName,
                                      @NotEmpty String currency,
                                      @NotNull BigDecimal accountBalance,
                                      @NotNull Boolean treasury) { }
