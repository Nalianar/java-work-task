package com.test.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.demo.dto.TransactionDto;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_card_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Card recipient;

    @ManyToOne
    @JoinColumn(name = "sender_card_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Card sender;

    private BigDecimal transactionAmount;

    public static TransactionDto toDto(Transaction transaction){
        return TransactionDto.builder()
                .transactionId(transaction.id)
                .transactionAmount(transaction.transactionAmount)
                .currency(transaction.recipient.getCurrency())
                .recipientId(transaction.recipient.getId())
                .senderId(transaction.sender.getId())
                .build();
    }

}

