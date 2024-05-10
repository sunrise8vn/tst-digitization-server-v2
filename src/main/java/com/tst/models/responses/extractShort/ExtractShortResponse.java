package com.tst.models.responses.extractShort;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExtractShortResponse {
    private Long id;
    private String folderPath;
    private String fileName;
    private String registrationType;
    private String inputStatus;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime importedAt;

    private String checkedStatus;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime checkedAt;

}
