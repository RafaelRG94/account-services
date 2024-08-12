package com.example.accountservices.account;

import com.example.accountservices.account.dto.AccountDto;
import com.example.accountservices.account.dto.CreateAccountRequestDto;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.account.model.CreateAccountRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.example.accountservices.account.AccountMapper.accountToDto;
import static com.example.accountservices.account.AccountMapper.createAccountRequestDtoToModel;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, path = "/accounts")
public class AccountResource {
    private final AccountService accountService;

    @Autowired
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountDto> getAllAccounts() {
        return accountService.findAll()
                .stream()
                .map(AccountMapper::accountToDto)
                .toList();
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccountById(@PathVariable(name = "accountId") UUID accountId) {
        Account account = accountService.findByIdOrThrow(accountId);
        return accountToDto(account);
    }

    @GetMapping("/search")
    public AccountDto getAccountByName(@RequestParam(value = "name") String accountName) {
        Account account = accountService.findByNameOrThrow(accountName);
        return accountToDto(account);
    }

    @PostMapping
    @ResponseStatus(CREATED) // Check here if the account name exists
    public AccountDto createAccount(@RequestBody @Valid CreateAccountRequestDto createAccountRequestDto) {
        CreateAccountRequest createAccountRequest = createAccountRequestDtoToModel(createAccountRequestDto);
        Account account = accountService.createAccount(createAccountRequest);

        return accountToDto(account);
    }
}
