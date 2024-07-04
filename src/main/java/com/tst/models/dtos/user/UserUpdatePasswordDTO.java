package com.tst.models.dtos.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdatePasswordDTO {
    @NotBlank(message = "{user.password.not_empty}")
    @Size(max = 50, message = "Mật khẩu tối đa 50 ký tự")
    private String password;

    private String retypePassword;
}
