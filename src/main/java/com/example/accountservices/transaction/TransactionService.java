package com.example.accountservices.transaction;

import com.example.accountservices.account.AccountRepository;
import com.example.accountservices.account.exception.AccountNotFoundException;
import com.example.accountservices.account.model.Account;
import com.example.accountservices.transaction.exception.TransactionBadRequestException;
import com.example.accountservices.transaction.exception.TransactionNotFoundException;
import com.example.accountservices.transaction.exception.NotEnoughAccountBalanceException;
import com.example.accountservices.transaction.model.CreateTransactionRequest;
import com.example.accountservices.transaction.model.Transaction;
import jakarta.transaction.Transactional;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    ExchangeRateProvider rateProvider = MonetaryConversions.getExchangeRateProvider();

    @Autowired
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    Transaction findByIdOrThrow(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    Transaction createTransaction(CreateTransactionRequest createTransactionRequest) {
        UUID senderAccountId = createTransactionRequest.getSenderAccountId();
        Account senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new AccountNotFoundException(senderAccountId));
        Money transactionMoney = Money.of(createTransactionRequest.getTransactionAmount(),
                senderAccount.getAccountBalance().getCurrency());
        if (senderAccountId == createTransactionRequest.getReceiverAccountId()) {
            throw new TransactionBadRequestException();
        } else {
            verifyAccountHasEnoughBalanceOrThrow(senderAccount, transactionMoney);
            UUID receiverAccountId = createTransactionRequest.getReceiverAccountId();
            Account receiverAccount = accountRepository.findById(receiverAccountId)
                    .orElseThrow(() -> new AccountNotFoundException(receiverAccountId));

            saveAccountTransaction(senderAccount, receiverAccount, transactionMoney);

            Transaction transaction = createTransactionRequestToNewTransaction(createTransactionRequest, transactionMoney);
            return transactionRepository.save(transaction);
        }
    }

    @Transactional
    private void saveAccountTransaction(Account senderAccount, Account receiverAccount, Money transactionAmount) {
        if(senderAccount.getAccountBalance().getCurrency().equals(receiverAccount.getAccountBalance().getCurrency())) {
            senderAccount.setAccountBalance(senderAccount
                    .getAccountBalance()
                    .subtract(transactionAmount)
            );
            receiverAccount.setAccountBalance(receiverAccount
                    .getAccountBalance()
                    .add(transactionAmount)
            );
        } else {
            CurrencyUnit receiverAccountCurrency = receiverAccount.getAccountBalance().getCurrency();
            CurrencyConversion currencyConversionToReceiverAccountCurrency = rateProvider.getCurrencyConversion(receiverAccountCurrency);
            MonetaryAmount transactionAmountInReceiverAccountCurrency = transactionAmount.with(currencyConversionToReceiverAccountCurrency);

            senderAccount.setAccountBalance(senderAccount
                    .getAccountBalance()
                    .subtract(transactionAmount)
            );
            receiverAccount.setAccountBalance(receiverAccount
                    .getAccountBalance()
                    .add(transactionAmountInReceiverAccountCurrency)
            );
        }

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
    }

    private void verifyAccountHasEnoughBalanceOrThrow(Account account, Money transactionAmount) {
        if (!account.getTreasury() && account.getAccountBalance().isLessThan(transactionAmount)) {
            throw new NotEnoughAccountBalanceException(account.getAccountId(), transactionAmount);
        }
    }

    private Transaction createTransactionRequestToNewTransaction(CreateTransactionRequest createTransactionRequest, Money transactionMoney) {
        return new Transaction(UUID.randomUUID(),
                createTransactionRequest.getSenderAccountId(),
                createTransactionRequest.getReceiverAccountId(),
                transactionMoney);
    }
}
