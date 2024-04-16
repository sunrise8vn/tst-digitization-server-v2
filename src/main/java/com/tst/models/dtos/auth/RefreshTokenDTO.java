package com.tst.models.dtos.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenDTO {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
}
