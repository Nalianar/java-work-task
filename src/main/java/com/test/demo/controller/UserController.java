package com.test.demo.controller;

import com.test.demo.dto.UserDto;
import com.test.demo.form.UserForm;
import com.test.demo.model.User;
import com.test.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public User saveUser(@Validated @RequestBody UserForm userForm) {
        return userService.createUser(userForm);
    }

//    @PostAuthorize("hasPermission(returnObject, )") dunno how to send permission in app
    @GetMapping("/top-users")
    public Set<UserDto> returnTopUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.returnTopUsers();
    }

    @GetMapping("/top-users-sql")
    public Set<UserDto> returnTopUsersSQL() {
        return userService.returnTopUsersSQL();
    }

    @PostMapping("/login")
    public User login(@Validated @RequestBody UserForm userForm) {
        return userService.login(userForm);
    }
}
