package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationNumberBookDTO {

    @NotBlank(message = "ID phường xã là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "ID phường xã phải là số")
    private String ward_id;

    @NotBlank(message = "Loại sổ là bắt buộc")
    private String registration_type_code;

    @NotBlank(message = "Khổ giấy là bắt buộc")
    private String paper_size_code;

    @NotBlank(message = "Năm của sổ là bắt buộc")
    @Pattern(regexp = "^(19[0-9]{2}|20[0-9]{2})(-(19[0-9]{2}|20[0-9]{2}))?$", message = "Năm của sổ phải có dạng 1965 hoặc 2007-2009")
    private String registration_date_code;

    @NotBlank(message = "Số quyển sổ là bắt buộc")
    @Pattern(regexp = "^\\d{2}$", message = "Số quyển sổ phải là 2 chữ số")
    private String number_book_code;

    private MultipartFile cover_file;

}
