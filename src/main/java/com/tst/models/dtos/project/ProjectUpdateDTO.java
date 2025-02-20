package com.tst.models.dtos.project;

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
public class ProjectUpdateDTO {
    @NotBlank(message = "Tên dự án là bắt buộc")
    @Size(max = 35, message = "Tên dự án tối đa là 35 ký tự")
    private String name;

    @Size(max = 100, message = "Mô tả dự án tối đa là 100 ký tự")
    private String description;
}
