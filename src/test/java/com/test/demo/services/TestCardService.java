package com.test.demo.services;

import com.test.demo.dto.CardDto;
import com.test.demo.model.Card;
import com.test.demo.model.User;
import com.test.demo.repositories.CardRepository;
import com.test.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static com.test.demo.model.Currency.USD;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestCardService {

    public static final int UNKNOWN_USER_ID = 2;
    @InjectMocks
    CardService cardService;
    @Mock
    CardRepository cardRepository;
    @Mock
    UserRepository userRepository;
    @Captor
    ArgumentCaptor<Card> cardCapture;
    public static final long TEST_USER_ID = 1;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void init() {
    }

    @Test
    public void createCardTest() {
        CardDto testCardDto = new CardDto(USD);
        User user = new User();
        user.setId(TEST_USER_ID);

        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));

        cardService.createCard(TEST_USER_ID, testCardDto);

        Mockito.verify(cardRepository).save(cardCapture.capture());
        Mockito.verify(userRepository).findById(TEST_USER_ID);
        Assert.assertEquals(cardCapture.getValue().getCurrency(), testCardDto.getCurrency());
        Assert.assertEquals(cardCapture.getValue().getMoney(), BigDecimal.ZERO);
    }

    @Test
    public void testCreateCardWithUnknownUser() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Cannot find user with id " + UNKNOWN_USER_ID);

        cardService.createCard(UNKNOWN_USER_ID, new CardDto(USD));
    }
}
