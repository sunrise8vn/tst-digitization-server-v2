package com.tst.models.dtos.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Size(max = 100, message = "Tên người dùng tối đa 100 ký tự")
    private String fullName;

    @Size(max = 10, message = "Số điện thoại tối đa 10 ký tự")
    private String phoneNumber;

    @NotBlank(message = "{user.password.not_empty}")
    private String password;

    private String retypePassword;

    @NotNull(message = "Vai trò không được rỗng")
    @Size(min = 1, message = "Vai trò là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Mã vai trò phải là số")
    private String roleCode;

    @NotNull(message = "Dự án là không được rỗng")
    @Size(min = 1, message = "Dự án là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Mã dự án phải là số")
    private String projectId;
}
