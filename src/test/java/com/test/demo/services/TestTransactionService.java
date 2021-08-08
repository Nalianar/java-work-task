package com.test.demo.services;

import com.test.demo.form.TransactionForm;
import com.test.demo.model.Card;
import com.test.demo.model.Transaction;
import com.test.demo.repositories.CardRepository;
import com.test.demo.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestTransactionService {

    public static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal(200);
    public static final Long RECIPIENT_ID = 1L;
    public static final Long SENDER_ID = 2L;
    public static final Long UNKNOWN_CARD_ID= 212L;
    public static final BigDecimal MONEY_ON_CARD = new BigDecimal(100);

    @InjectMocks
    TransactionService transactionService;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    CardRepository cardRepository;
    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Mock
    private CardService cardService;

    @Before
    public void init() {
    }

    @Test
    public void depositCardTest(){
        TransactionForm transactionForm = new TransactionForm(TRANSACTION_AMOUNT, RECIPIENT_ID, null);
        Card recipientCard = generateCard(RECIPIENT_ID);

        when(cardRepository.findById(RECIPIENT_ID)).thenReturn(Optional.of(recipientCard));

        cardService.depositCard(transactionForm);

        verify(cardRepository).findById(RECIPIENT_ID);
        verify(cardRepository).save(recipientCard);
        verify(transactionRepository).save(transactionCaptor.capture());

        assertEquals(transactionCaptor.getValue().getTransactionAmount(), transactionForm.getTransactionAmount());
        assertEquals(recipientCard.getMoney(), MONEY_ON_CARD.add(TRANSACTION_AMOUNT));
    }

    @Test
    public void transferMoneyTest(){
        TransactionForm transactionForm = new TransactionForm(TRANSACTION_AMOUNT, RECIPIENT_ID, SENDER_ID);
        Card resipient = generateCard(RECIPIENT_ID);
        Card sender = generateCard(SENDER_ID);

        when(cardRepository.findById(RECIPIENT_ID)).thenReturn(Optional.of(resipient));
        when(cardRepository.findById(SENDER_ID)).thenReturn(Optional.of(sender));

        cardService.transferMoney(transactionForm);

        verify(cardRepository).save(resipient);
        verify(cardRepository).save(sender);
        verify(cardRepository).findById(RECIPIENT_ID);
        verify(cardRepository).findById(SENDER_ID);
        verify(transactionRepository).save(transactionCaptor.capture());

        assertEquals(transactionCaptor.getValue().getTransactionAmount(), transactionForm.getTransactionAmount());
        assertEquals(resipient.getMoney(), MONEY_ON_CARD.add(transactionForm.getTransactionAmount()));
        assertEquals(sender.getMoney(), MONEY_ON_CARD.subtract(transactionForm.getTransactionAmount()));

    }

    @Test
    public void testCardDepositException(){
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Cannot find recipient card with id " + UNKNOWN_CARD_ID);

        cardService.depositCard(new TransactionForm(new BigDecimal(100), UNKNOWN_CARD_ID, null));
    }

    @Test
    public void testTransferMoneyExceptionRecipient(){
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Cannot find recipient card with id " + UNKNOWN_CARD_ID);

        cardService.transferMoney(new TransactionForm(new BigDecimal(100), UNKNOWN_CARD_ID, SENDER_ID));
    }

    @Test
    public void testTransferMoneyExceptionSender(){
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Cannot find sender card with id " + UNKNOWN_CARD_ID);

        Card card = generateCard(RECIPIENT_ID);

        when(cardRepository.findById(RECIPIENT_ID)).thenReturn(Optional.of(card));

        cardService.transferMoney(new TransactionForm(new BigDecimal(100), RECIPIENT_ID, UNKNOWN_CARD_ID));
    }


    private Card generateCard(Long recipientId) {
        Card card = new Card();
        card.setId(recipientId);
        card.setMoney(MONEY_ON_CARD);
        return card;
    }
}
