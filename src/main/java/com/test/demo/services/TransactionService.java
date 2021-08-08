package com.test.demo.services;

import com.test.demo.dto.TransactionDto;
import com.test.demo.model.Card;
import com.test.demo.model.Currency;
import com.test.demo.model.Transaction;
import com.test.demo.model.User;
import com.test.demo.repositories.CardRepository;
import com.test.demo.repositories.TransactionRepository;
import com.test.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Service
public class TransactionService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;


    public Set<TransactionDto> getAllUserTransactionsById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Cannot find user by id " + userId));
        Set<Card> cardSet = user.getCards();
        Set<Transaction> transactions = cardSet.stream()
                .map(Card::getRecipientTransaction)
                .flatMap(Set::stream)
                .collect(toSet());
        transactions.addAll(cardSet.stream()
                .map(Card::getSenderTransaction)
                .flatMap(Set::stream)
                .sorted(Comparator.comparingLong(Transaction::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));



        return transactions.stream().map(Transaction::toDto).collect(toSet());
    }

    public BigDecimal getAllMoneyFromUserByCurrency(long userId, Currency currency) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("cannot find user by " + userId))
                .getCards()
                .stream()
                .filter(x->x.getCurrency().equals(currency))
                .map(Card::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Set<Transaction> getAllTransactionsByCurrency(Currency currency) {
        Set<Card> cards = cardRepository.getAllByCurrency(currency);
        Set<Transaction> transactions = cards.stream()
                .map(Card::getSenderTransaction)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        transactions.addAll(cards.stream()
                .map(Card::getRecipientTransaction)
                .flatMap(Set::stream)
                .collect(Collectors.toSet()));
        return transactions;
    }
}
