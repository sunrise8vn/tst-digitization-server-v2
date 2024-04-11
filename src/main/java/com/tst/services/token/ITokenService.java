package com.tst.services.token;

import com.tst.models.entities.Token;
import com.tst.models.entities.User;

public interface ITokenService {

    Token addToken(User user, String token);
    Token refreshToken(String refreshToken, User user) throws Exception;
}
