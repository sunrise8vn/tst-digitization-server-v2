package com.tst.models.responses.locationRegion;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LocationProvinceResponse {
    private Long id;
    private String name;
    private String code;
    private String divisionType;
    private String phoneCode;

}
