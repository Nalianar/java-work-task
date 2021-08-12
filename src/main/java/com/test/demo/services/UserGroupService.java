package com.test.demo.services;

import com.test.demo.form.UserGroupForm;
import com.test.demo.form.UserToGroupForm;
import com.test.demo.model.User;
import com.test.demo.model.UserGroup;
import com.test.demo.repositories.UserGroupRepository;
import com.test.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class UserGroupService {

    @Autowired
    private UserGroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserGroup createGroup(UserGroupForm userGroupForm) {
       return groupRepository.save(UserGroup.builder()
                .role(userGroupForm.getRole())
                .url(userGroupForm.getUrl())
                .build());
    }

    @Transactional
    public UserGroup connectUserToGroup(UserToGroupForm userToGroup) {
        UserGroup userGroup = groupRepository.findByRole(userToGroup.getRole())
                .orElseThrow(()->new RuntimeException("Cannot find group" + userToGroup.getRole()));

        Set<User> users = userGroup.getUsers();
        users.add(userRepository.findByLogin(userToGroup.getUser())
                .orElseThrow(()->new RuntimeException("Cannot find user with name" + userToGroup.getUser())));

        userGroup.setUsers(users);
        return groupRepository.save(userGroup);
    }

    @Transactional
    public User connectGroupToUser(UserToGroupForm userToGroup) {
        User user = userRepository.findByLogin(userToGroup.getUser())
                .orElseThrow(()->new RuntimeException("Cannot find user " + userToGroup.getUser()));

        Set<UserGroup> groups = user.getGroups();
        groups.add(groupRepository.findByRole(userToGroup.getRole())
                .orElseThrow(()->new RuntimeException("Cannot find role" + userToGroup.getRole())));

        user.setGroups(groups);
        return userRepository.save(user);
    }


    public UserGroup changeGroupUrl(UserGroupForm groupForm) {
        UserGroup userGroup = groupRepository.findByRole(groupForm.getRole())
                .orElseThrow(()-> new RuntimeException("Cannot find role " + groupForm.getRole()));
        userGroup.setUrl(groupForm.getUrl());
        return groupRepository.save(userGroup);
    }
}
