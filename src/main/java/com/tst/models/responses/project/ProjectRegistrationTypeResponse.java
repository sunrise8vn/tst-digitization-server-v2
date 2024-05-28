package com.tst.models.responses.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.enums.ERegistrationType;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectRegistrationTypeResponse {
    private Long id;
    private ERegistrationType code;
}
