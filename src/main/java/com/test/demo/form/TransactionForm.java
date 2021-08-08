package com.test.demo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TransactionForm {
    private BigDecimal transactionAmount;
    private Long recipientId;
    private Long senderId;
}
