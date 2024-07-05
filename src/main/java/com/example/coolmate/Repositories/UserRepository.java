package com.example.coolmate.Repositories;

import com.example.coolmate.Models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByRoleId(@Param("roleId") int roleId);
}
