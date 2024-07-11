package com.tst.models.dtos.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserChangeInfoDTO {
    @NotBlank(message = "Tên người dùng không được rỗng")
    @Size(max = 100, message = "Tên người dùng tối đa 100 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được rỗng")
    @Size(max = 10, message = "Số điện thoại tối đa 10 ký tự")
    private String phoneNumber;

    @Size(max = 250, message = "Địa chỉ tối đa là 250 ký tự")
    private String address;

    @NotBlank(message = "Vai trò là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Mã vai trò phải là số")
    private String roleCode;
}
