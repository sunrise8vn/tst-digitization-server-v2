package com.tst.models.dtos.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExtractDataDTO {
    @NotEmpty(message = "Loại tài liệu là bắt buộc")
    private String registrationType;

    @NotNull(message = "Số lượng trích xuất là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Số lượng trích xuất phải là số lớn hơn 0")
    private String countExtract;
}
