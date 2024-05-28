package com.tst.models.responses.statistic;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatisticProjectProvinceResponse {
    private Long id;
    private String folderName;
    private long totalFiles;
    private Long totalSizes;
    private Long a0;
    private Long a1;
    private Long a2;
    private Long a3;
    private Long a4;
    private Long convertA4;
}
