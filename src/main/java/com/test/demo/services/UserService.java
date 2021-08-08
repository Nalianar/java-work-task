package com.test.demo.services;

import com.test.demo.dto.UserDto;
import com.test.demo.form.UserForm;
import com.test.demo.model.Currency;
import com.test.demo.model.User;
import com.test.demo.repositories.CardRepository;
import com.test.demo.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExchangeRateService exchangeService;

    public User createUser(UserForm user) {
        User us = new User();
        us.setLogin(user.getLogin());
        us.setPassword(user.getPassword());
        String token = getJWTToken(user.getLogin());
        us.setToken(token);
        return userRepository.save(us);
    }

    public Set<UserDto> returnTopUsers() {
        List<User> users = userRepository.findAll();

        Map<User, BigDecimal> userToAllMoneyMap = users.stream()
                .collect(toMap(Function.identity(), user -> user.getCards().stream()
                        .map(card -> Pair.of(card.getCurrency(), card.getMoney()))
                        .reduce(Pair.of(Currency.EUR, BigDecimal.ZERO),
                                this::convertCurrencyAndAdd).getSecond())); // TODO: 03.08.2021 make pretty

       return userToAllMoneyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> {
                    UserDto userDto = User.toDto(entry.getKey());
                    userDto.setMoney(entry.getValue().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
                    return userDto;
                })
                .limit(5)
                .collect(toSet());
    }

    private Pair<Currency, BigDecimal> convertCurrencyAndAdd(Pair<Currency, BigDecimal> p1, Pair<Currency, BigDecimal> p2) {
        return Pair.of(Currency.EUR, exchangeService.convertToEuro(p1.getFirst(), p1.getSecond())
                .add(exchangeService.convertToEuro(p2.getFirst(), p2.getSecond())));
    }

    public Set<UserDto> returnTopUsersSQL() {
        return userRepository.returnTopUsers();
    }

    public User login(UserForm userForm) {

        User user = new User();
        user.setId(userRepository.findByLogin(userForm.getLogin()).getId());
        user.setLogin(userRepository.findByLogin(userForm.getLogin()).getLogin());
        user.setToken(userRepository.findByLogin(userForm.getLogin()).getToken());
        user.setPassword(userRepository.findByLogin(userForm.getLogin()).getPassword());

        if(user.getPassword().equals(userForm.getPassword()))
            return user;

        return null;
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("JWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

    //get users, get cards by user, get all cards money and sum, sort users return 5
}
