package com.tst.repositories;

import com.tst.models.entities.User;
import com.tst.models.responses.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT new com.tst.models.responses.user.UserResponse(" +
                "us.id, " +
                "us.username, " +
                "ui.fullName, " +
                "ui.email, " +
                "ui.phoneNumber, " +
                "ui.address, " +
                "us.activated, " +
                "us.role " +
            ") " +
            "FROM User AS us " +
            "LEFT JOIN UserInfo AS ui " +
            "ON us = ui.user " +
            "WHERE us.username LIKE %:keyWord% " +
            "OR ui.fullName LIKE %:keyWord% " +
            "OR ui.email LIKE %:keyWord% " +
            "OR ui.phoneNumber LIKE %:keyWord% " +
            "OR ui.address LIKE %:keyWord% "
    )
    Page<UserResponse> findAllUserResponse(@Param("keyWord") String keyWord, Pageable pageable);

}


