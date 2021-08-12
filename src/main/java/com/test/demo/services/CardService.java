package com.test.demo.services;

import com.test.demo.dto.CardDto;
import com.test.demo.form.TransactionForm;
import com.test.demo.model.Card;
import com.test.demo.model.Transaction;
import com.test.demo.repositories.CardRepository;
import com.test.demo.repositories.TransactionRepository;
import com.test.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ExchangeRateService exchangeService;

    public Card createCard(long userId, CardDto cardDto) {
        Card card = new Card();
        card.setMoney(BigDecimal.ZERO);
        card.setCurrency(cardDto.getCurrency());
        card.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cannot find user with id " + userId)));
        isUserNotAuthWithCurrentCard(card);
        return cardRepository.save(card);
    }

    @Transactional
    public Transaction transferMoney(TransactionForm transactionForm){
        Card recipient = cardRepository.findById(transactionForm.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Cannot find recipient card with id " + transactionForm.getRecipientId()));
        Card sender = cardRepository.findById(transactionForm.getSenderId())
                .orElseThrow(() -> new RuntimeException("Cannot find sender card with id " + transactionForm.getSenderId()));

        isUserNotAuthWithCurrentCard(sender);

        Transaction tr = new Transaction();
        tr.setRecipient(recipient);
        tr.setSender(sender);
        tr.setTransactionAmount(transactionForm.getTransactionAmount());

        recipient.setMoney(recipient.getMoney().add(exchangeService.convertSenderToRecipientAmount(tr)));
        sender.setMoney(sender.getMoney().subtract(tr.getTransactionAmount()));

        cardRepository.save(recipient);
        cardRepository.save(sender);

        return transactionRepository.save(tr);
    }

    private void isUserNotAuthWithCurrentCard(Card card) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()
                || !authentication.getName()
                .equals(card.getUser().getLogin())){
            throw new RuntimeException("User is not authenticated with card");
        }
    }


    @Transactional
    public Transaction depositCard(TransactionForm transactionForm){

        Card card = cardRepository.findById(transactionForm.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Cannot find recipient card with id " + transactionForm.getRecipientId()));

        isUserNotAuthWithCurrentCard(card);
        Transaction transaction = new Transaction();
        transaction.setRecipient(card);
        transaction.setTransactionAmount(transactionForm.getTransactionAmount());

        card.setMoney(card.getMoney().add(transactionForm.getTransactionAmount()));
        cardRepository.save(card);

        return transactionRepository.save(transaction);
    }
}
