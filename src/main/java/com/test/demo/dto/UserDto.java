package com.test.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String login;
    private BigDecimal money;
    private int numOfCards;
    private String token;
    private String password;


    public UserDto(Long id, String login, BigDecimal money) {
        this.id = id;
        this.login = login;
        this.money = money;
    }

    public UserDto(Long id, String login, String token, String password) {
        this.id = id;
        this.login = login;
        this.token = token;
        this.password = password;
    }
}
