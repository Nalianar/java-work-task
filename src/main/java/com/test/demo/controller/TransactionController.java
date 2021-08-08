package com.test.demo.controller;

import com.test.demo.dto.TransactionDto;
import com.test.demo.model.Currency;
import com.test.demo.model.Transaction;
import com.test.demo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/user/{userId}/")
    public Set<TransactionDto> returnAllUserTransactions(@PathVariable long userId) {
        return transactionService.getAllUserTransactionsById(userId);
    }

    @GetMapping("/user/{userId}/currency/{currency}")
    public BigDecimal returnAllUserTransactions(@PathVariable long userId, @PathVariable Currency currency) {
        return transactionService.getAllMoneyFromUserByCurrency(userId, currency);
    }

    @GetMapping("/currency/{currency}")
    public Set<Transaction> returnAllUserTransactions(@PathVariable Currency currency){
        return transactionService.getAllTransactionsByCurrency(currency);
    }
}
