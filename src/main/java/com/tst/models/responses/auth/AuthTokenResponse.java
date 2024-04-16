package com.tst.models.responses.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
