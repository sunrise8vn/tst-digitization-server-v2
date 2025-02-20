package com.tst.models.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenDTO {
    @NotBlank
    private String refresh_token;
}
