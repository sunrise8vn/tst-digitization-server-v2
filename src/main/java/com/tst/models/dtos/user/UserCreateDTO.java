package com.tst.models.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateDTO {

    @NotBlank(message = "{user.username.not_empty}")
    private String username;

    @NotBlank(message = "{user.password.not_empty}")
    private String password;

    private String retype_password;

    @NotNull(message = "Vai trò là bắt buộc")
    private Long role_id;
}
