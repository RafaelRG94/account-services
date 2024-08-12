package com.example.accountservices.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.javamoney.moneta.Money;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // I'm not 100% sure, but I think it will not be necessary because I'll set the UUID manually
    UUID accountId;

    @Column(unique = true)
    @NotEmpty
    String name;

    @NotNull
    Money accountBalance;

    @NotNull
    Boolean treasury;

}
