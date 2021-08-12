package com.test.demo.repositories;

import com.test.demo.model.User;
import com.test.demo.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByRole(String role);
}
