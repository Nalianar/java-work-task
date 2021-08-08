package com.test.demo.dto;

import com.test.demo.model.Currency;
import com.test.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Currency currency;
}
