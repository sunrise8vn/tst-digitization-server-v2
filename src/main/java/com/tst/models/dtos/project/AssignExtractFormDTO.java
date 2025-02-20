package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AssignExtractFormDTO {

    @NotBlank(message = "ID dự án là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là số")
    private String project_id;

    @NotBlank(message = "Tổng số phiếu là bắt buộc")
    @Pattern(regexp = "^(?!0+$)[0-9]*[02468]$", message = "Tổng số phiếu phải là số chẵn lớn hơn 0")
    private String total_count;

    @NotEmpty(message = "Danh sách tài khoản người dùng là bắt buộc")
    private List<String> users;
}
