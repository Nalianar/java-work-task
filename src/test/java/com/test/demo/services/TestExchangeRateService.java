package com.test.demo.services;


import com.test.demo.model.Card;
import com.test.demo.model.Currency;
import com.test.demo.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

@RunWith(MockitoJUnitRunner.class)
public class TestExchangeRateService {

    public static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal(50);
    @InjectMocks
    ExchangeRateService exchangeRateService;

    @Before
    public void init() {
    }

    @Test
    public void convertSenderToRecipientDifferentCurrenciesTest(){
        Transaction transaction = new Transaction();
        Card recipient = new Card();
        Card sender = new Card();
        recipient.setCurrency(Currency.USD);
        sender.setCurrency(Currency.GBP);
        transaction.setRecipient(recipient);
        transaction.setSender(sender);
        transaction.setTransactionAmount(TRANSACTION_AMOUNT);
        double val = exchangeRateService.convertSenderToRecipientAmount(transaction).doubleValue();


        Assert.assertEquals(val, 69.5, 0.0);
    }

}
