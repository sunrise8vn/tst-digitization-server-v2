package com.tst.models.responses.accessPoint;

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
public class ImporterAccessPointHistoryResponse {

    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;
    private Long countExtractShort;
    private Long countExtractFull;
    private Long countSuccessExtractShort = 0L;
    private Long countSuccessExtractFull = 0L;
    private Long countErrorExtractShort = 0L;
    private Long countErrorExtractFull = 0L;
    private Long countRevokeExtractShort = 0L;
    private Long countRevokeExtractFull = 0L;
    private Long totalCount;

}
