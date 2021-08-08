package com.test.demo.repositories;

import com.test.demo.dto.UserDto;
import com.test.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u")
    List<User> findAll();

    @Query("SELECT new com.test.demo.dto.UserDto(u.id, u.login, SUM(c.money)) " +
            "FROM User u " +
            "JOIN Card c " +
            "ON u.id = c.user.id GROUP BY u.id " +
            "ORDER BY sum(c.money) DESC ")
    Set<UserDto> returnTopUsers();

    @Query("SELECT new com.test.demo.dto.UserDto(u.id, u.login, u.token, u.password) " +
            "FROM User u " +
            "WHERE u.login = ?1")
    UserDto findByLogin(String username);
}
