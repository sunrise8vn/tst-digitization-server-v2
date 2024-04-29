package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectNumberBookTempOrganizationDTO {

//    @NotBlank(message = "Loại sổ đăng ký là bắt buộc")
//    private String registration_type_code;

    @NotBlank(message = "ID tập tin là bắt buộc")
    @Pattern(regexp = "\\d+", message = "ID tập tin phải là số")
    private String id;

    @NotBlank(message = "Ngày tháng năm là bắt buộc")
    private String day_month_year;

    @NotBlank(message = "Số đăng ký là bắt buộc")
    @Pattern(regexp = "\\d{1,3}", message = "ID số đăng ký phải là số từ 1 đến 3 ký tự")
    private String number;
}
