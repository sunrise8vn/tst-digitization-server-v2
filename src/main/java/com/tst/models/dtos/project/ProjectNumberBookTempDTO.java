package com.tst.models.dtos.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectNumberBookTempDTO {

    @NotNull(message = "ID quyển số là bắt buộc")
    @Pattern(regexp = "\\d+", message = "ID quyển số phải là số")
    private String projectNumberBookId;

    private List<MultipartFile> files;
}
