package com.tst.models.responses.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReportImporterImportedResponse {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate importedAt;
    private Long fullLater;
    private Long shortLater;
    private Long fullImported;
    private Long shortImported;
}
