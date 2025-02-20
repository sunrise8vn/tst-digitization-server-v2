package com.tst.models.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginDTO {

    @NotBlank(message = "{user.username.not_empty}")
    private String username;

    @NotBlank(message = "{user.password.not_empty}")
    private String password;

}
