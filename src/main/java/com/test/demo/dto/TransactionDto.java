package com.test.demo.dto;

import com.test.demo.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long transactionId;
    private BigDecimal transactionAmount;
    private Long recipientId;
    private Long senderId;
    private Currency currency;
}
