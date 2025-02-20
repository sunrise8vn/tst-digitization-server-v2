package com.tst.models.responses.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String folder;
    private String description;
    private Long a0;
    private Long a1;
    private Long a2;
    private Long a3;
    private Long a4;
    private Long convertA4;
    private Long totalSize;

}
