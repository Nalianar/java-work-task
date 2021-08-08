package com.test.demo.services;

import com.test.demo.dto.ExchangeRateDto;
import com.test.demo.model.Card;
import com.test.demo.model.Currency;
import com.test.demo.model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@Service
public class ExchangeRateService {

    public static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public BigDecimal convertSenderToRecipientAmount(Transaction transaction) {
        BigDecimal senderToRecipientRate = calculateSenderToRecipientRate(transaction.getSender(), transaction.getRecipient());
        return transaction.getTransactionAmount().multiply(senderToRecipientRate);
    }

    private BigDecimal calculateSenderToRecipientRate(Card sender, Card recipient) {
        Map<String, BigDecimal> ratesMap = getRatesMap();

        BigDecimal recipientRate = getRates(recipient.getCurrency(), ratesMap);
        BigDecimal senderRate = getRates(sender.getCurrency(), ratesMap);

        return recipientRate.divide(senderRate, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getRates(Currency currency, Map<String, BigDecimal> rates) {
        return rates.get(Optional.ofNullable(currency)
                .map(Currency::getName)
                .orElseThrow(() -> new RuntimeException("Cannot retrieve rate")));
    }

    public BigDecimal convertToEuro(Currency currency, BigDecimal amount) {
        if (amount.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
        return getRates(currency, getRatesMap()).divide(amount, 2, RoundingMode.HALF_UP);
    }

    private Map<String, BigDecimal> getRatesMap() {
        return Optional.ofNullable(REST_TEMPLATE.getForObject(
                "http://api.exchangeratesapi.io/v1/2021-07-28?access_key=a64e20394740fe064074f42e3d023b27", ExchangeRateDto.class))
                .orElseThrow(() -> new RuntimeException("Cannot retrieve rate"))
                .getRates();
    }


}
