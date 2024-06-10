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
public class CountExtractFormNotAssignResponse {
    private Long countParentsChildrenShort;
    private Long countParentsChildrenFull;
    private Long countBirthShort;
    private Long countBirthFull;
    private Long countMarryShort;
    private Long countMarryFull;
    private Long countWedlockShort;
    private Long countWedlockFull;
    private Long countDeathShort;
    private Long countDeathFull;
//    private Long totalCount;
}
