package com.example.accountservices.config.h2;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, String>  {
    private static final String DELIMITER = ":";

    @Override
    public String convertToDatabaseColumn(Money money) {
        if (money == null) {
            return null;
        }

        // Save using format "currency:amount" -> "USD:100.50"
        return money.getCurrency().getCurrencyCode() + DELIMITER + money.getNumber();
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }

        try {
            String[] parts = dbData.split(DELIMITER);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Error while converting to Money, incorrect data: " + dbData);
            }

            CurrencyUnit currency = Monetary.getCurrency(parts[0]);
            BigDecimal amount = new BigDecimal(parts[1]);
            return Money.of(amount, currency);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error while converting to Money: " + dbData, e);
        }
    }
}
