package com.tst.models.responses.locationRegion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LocationDistrictResponse {
    private Long id;
    private String name;
    private String code;

    @JsonProperty("division_type")
    private String divisionType;

    @JsonProperty("short_code")
    private String shortCode;
}
