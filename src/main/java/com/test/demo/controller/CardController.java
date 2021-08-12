package com.test.demo.controller;

import com.test.demo.dto.CardDto;
import com.test.demo.form.TransactionForm;
import com.test.demo.model.Card;
import com.test.demo.model.Transaction;
import com.test.demo.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class  CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/save/{userId}")
    public Card saveCard(@PathVariable long userId, @Validated @RequestBody CardDto cardDto) {
        return cardService.createCard(userId, cardDto);
    }

    @PostMapping("/transfer-money")
    public Transaction transferMoney(@Validated @RequestBody TransactionForm transactionForm) {
        return cardService.transferMoney(transactionForm);
    }


    @PostMapping("/deposit-card")
    public Transaction depositCard(@Validated @RequestBody TransactionForm transactionForm) {
        return cardService.depositCard(transactionForm);
    }
}
