package com.example.accountservices.transaction;

import com.example.accountservices.transaction.dto.CreateTransactionRequestDto;
import com.example.accountservices.transaction.dto.TransactionDto;
import com.example.accountservices.transaction.model.CreateTransactionRequest;
import com.example.accountservices.transaction.model.Transaction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.example.accountservices.transaction.TransactionMapper.createTransactionRequestDtoToModel;
import static com.example.accountservices.transaction.TransactionMapper.transactionToDto;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, path = "/transactions")
public class TransactionResource {
    private final TransactionService transactionService;

    @Autowired
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    List<TransactionDto> getAllTransaction() {
        return transactionService.findAll()
                .stream()
                .map(TransactionMapper::transactionToDto)
                .toList();
    }

    @GetMapping("/{transactionId}")
    TransactionDto getTransactionById(@PathVariable(name = "transactionId") UUID transactionId) {
        Transaction transaction = transactionService.findByIdOrThrow(transactionId);
        return transactionToDto(transaction);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    TransactionDto createTransaction(@RequestBody @Valid CreateTransactionRequestDto createTransactionRequestDto) {
        CreateTransactionRequest createTransactionRequest = createTransactionRequestDtoToModel(createTransactionRequestDto);
        Transaction transaction = transactionService.createTransaction(createTransactionRequest);

        return transactionToDto(transaction);
    }
}

