package com.tst.models.responses.auth;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthLoginResponse {
    private AuthUserResponse user;
    private AuthTokenResponse token;

}
