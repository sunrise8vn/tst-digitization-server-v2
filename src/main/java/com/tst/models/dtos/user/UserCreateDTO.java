package com.tst.models.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("retype_password")
    private String retypePassword;

    @NotNull(message = "Vai trò là bắt buộc")
    @JsonProperty("role_id")
    private Long roleId;
}
