package com.tst.models.dtos.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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

    private String retypePassword;

    @NotBlank(message = "Vai trò là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Mã vai trò phải là số")
    private String roleCode;

    @NotBlank(message = "Dự án là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Mã dự án phải là số")
    private String projectId;
}
