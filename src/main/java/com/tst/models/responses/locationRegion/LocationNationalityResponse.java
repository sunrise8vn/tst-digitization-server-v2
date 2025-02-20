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
public class LocationNationalityResponse {
    private Long id;
    private String name;
    private String code;

    public LocationNationalityResponse(Long id, String name) {
        this.id = id;
        this.name = name;
        this.code = id.toString();
    }
}
