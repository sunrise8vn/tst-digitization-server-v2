package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationPointDTO {

    @NotBlank(message = "ID dự án là bắt buộc")
    @Pattern(regexp = "\\d+", message = "ID dự án phải là số")
    private String project_id;

    @NotBlank(message = "ID tỉnh/thành phố là bắt buộc")
    @Pattern(regexp = "\\d+", message = "ID tỉnh/thành phố phải là số")
    private String province_id;

    @NotBlank(message = "ID thành phố/quận/huyện là bắt buộc")
    @Pattern(regexp = "\\d+", message = "ID thành phố/quận/huyện phải là số")
    private String district_id;

    @NotBlank(message = "ID phường/xã là bắt buộc")
    @Pattern(regexp = "\\d+", message = "ID phường/xã phải là số")
    private String ward_id;

}
