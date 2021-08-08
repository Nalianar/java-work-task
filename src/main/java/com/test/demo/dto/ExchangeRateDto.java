package com.test.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateDto {
    private Map<String, BigDecimal> rates;
}
