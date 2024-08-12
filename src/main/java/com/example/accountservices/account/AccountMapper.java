package com.example.accountservices.account;

import com.example.accountservices.account.dto.AccountDto;
import com.example.accountservices.account.dto.CreateAccountRequestDto;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import lombok.experimental.UtilityClass;
import org.javamoney.moneta.Money;

@UtilityClass
public class AccountMapper {
    static CreateAccountRequest createAccountRequestDtoToModel(CreateAccountRequestDto createAccountRequestDto) {
        return new CreateAccountRequest(createAccountRequestDto.accountName(),
                Money.of(createAccountRequestDto.accountBalance(), createAccountRequestDto.currency()),
                createAccountRequestDto.treasury()); // Why must I add @AllArgsConstructor and @NoArgsConstructor annotation in the CreateAccountRequestDto class so that this instantiation doesn't fail?
    }

    static AccountDto accountToDto(Account account) {
        return new AccountDto(account.getAccountId(),
                account.getName(),
                account.getAccountBalance().getCurrency().toString(),
                account.getAccountBalance().getNumberStripped(),
                account.getTreasury());
    }
}
