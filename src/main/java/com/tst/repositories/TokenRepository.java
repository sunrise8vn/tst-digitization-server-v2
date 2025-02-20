package com.tst.repositories;

import com.tst.models.entities.Token;
import com.tst.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);

    Optional<Token> findByToken(String token);

    Optional<Token> findByRefreshToken(String token);
}


