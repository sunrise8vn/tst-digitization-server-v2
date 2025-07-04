package com.tst.services.token;

import com.tst.components.JwtTokenUtils;
import com.tst.components.LocalizationUtils;
import com.tst.exceptions.UnauthorizedException;
import com.tst.models.entities.Token;
import com.tst.models.entities.User;
import com.tst.models.entities.UserInfo;
import com.tst.repositories.TokenRepository;
import com.tst.repositories.UserInfoRepository;
import com.tst.utils.MessageKeys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final UserInfoRepository userInfoRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtil;
    private final LocalizationUtils localizationUtils;


    @Transactional
    @Override
    public Token addToken(User user, String token) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();

        // Số lượng token vượt quá giới hạn, xóa một token cũ
        if (tokenCount >= MAX_TOKENS) {
            Token tokenToDelete = userTokens.get(0);
            tokenRepository.delete(tokenToDelete);
        }

        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);

        // Tạo mới một token cho người dùng
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .build();

        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);

        return newToken;
    }

    @Transactional
    @Override
    public Token refreshToken(String refreshToken, User user) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_NOT_EXISTS));
        });

        if (existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(existingToken);
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_EXPIRED));
        }

        String fullName = null;

        Optional<UserInfo> userInfo = userInfoRepository.findByUser(user);

        if (userInfo.isPresent()) {
            fullName = userInfo.get().getFullName();
        }

        String token = jwtTokenUtil.generateToken(user, fullName);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        existingToken.setExpirationDate(expirationDateTime);
        existingToken.setToken(token);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }




}