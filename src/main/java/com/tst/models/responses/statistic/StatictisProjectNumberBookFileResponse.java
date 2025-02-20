package com.tst.models.responses.statistic;

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
public class StatictisProjectNumberBookFileResponse {
    private Long id;
    private String fileName;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private Long fileSize;
    private Integer a0;
    private Integer a1;
    private Integer a2;
    private Integer a3;
    private Integer a4;
    private Integer convertA4;
}
