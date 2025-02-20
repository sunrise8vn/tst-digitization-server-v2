package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectNumberBookFileDTO {

    @NotNull(message = "ID quyển số là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "ID quyển số phải là số lớn hơn 0")
    private String number_book_id;

    private List<MultipartFile> files;
}
