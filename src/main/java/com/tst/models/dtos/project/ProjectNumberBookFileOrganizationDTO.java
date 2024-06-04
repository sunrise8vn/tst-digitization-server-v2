package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectNumberBookFileOrganizationDTO {

    @NotBlank(message = "ID tập tin là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "ID tập tin phải là số lớn hơn 0")
    private String id;

    @NotBlank(message = "Ngày tháng năm là bắt buộc")
    private String day_month_year;

//    @NotBlank(message = "Số đăng ký là bắt buộc")
    @Pattern(regexp = "\\d{1,3}", message = "ID số đăng ký phải là số từ 1 đến 3 ký tự")
    private String number;
}
