package com.test.demo.controller;

import com.test.demo.form.UserForm;
import com.test.demo.form.UserGroupForm;
import com.test.demo.form.UserToGroupForm;
import com.test.demo.model.Currency;
import com.test.demo.model.User;
import com.test.demo.model.UserGroup;
import com.test.demo.services.UserGroupService;
import com.test.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-group")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    @PostMapping("/create-group")
    public UserGroup createUserGroup(@Validated @RequestBody UserGroupForm userGroupForm){
        return userGroupService.createGroup(userGroupForm);
    }

    @PostMapping("/connect-user-to-group")
    public UserGroup connectUserToGroup(@RequestBody UserToGroupForm userToGroup){
        return userGroupService.connectUserToGroup(userToGroup);
    }

    @PostMapping("/connect-group-to-user")
    public User connectGroupToUser(@RequestBody UserToGroupForm userToGroup){
        return userGroupService.connectGroupToUser(userToGroup);
    }

    @PostMapping("/change-group-url")
    public UserGroup changeGroupPermission(@RequestBody UserGroupForm groupForm){
        return userGroupService.changeGroupUrl(groupForm);
    }



}
