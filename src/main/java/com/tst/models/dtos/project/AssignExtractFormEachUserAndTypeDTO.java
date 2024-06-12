package com.tst.models.dtos.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AssignExtractFormEachUserAndTypeDTO {
    @NotEmpty(message = "Tài khoản người dùng là bắt buộc")
    private String username;

    private List<ExtractFormCountTypeDTO> extractFormTypes;
}
