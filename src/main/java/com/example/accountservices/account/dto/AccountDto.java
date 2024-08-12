package com.example.accountservices.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDto(UUID accountId,
                        String name,
                        String currency,
                        BigDecimal balance,
                        Boolean treasury) { }