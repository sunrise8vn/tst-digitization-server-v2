package com.tst.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenDTO {
    @NotBlank
    private String refreshToken;
}
