package com.tst.models.responses.report;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExtractFormImportedForManagerResponse {
    private Long accessPointId;
    private String fullName;
    private Long countExtractShort;
    private Long countExtractFull;
    private Long countExtractShortImported;
    private Long countExtractFullImported;
}
