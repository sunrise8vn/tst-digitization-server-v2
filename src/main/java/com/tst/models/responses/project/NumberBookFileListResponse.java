package com.tst.models.responses.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.ERegistrationType;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NumberBookFileListResponse {
    private Long id;
    private ERegistrationType registrationType;
    private EPaperSize paperSize;
    private String registrationDate;
    private String numberBookCode;
    private String fileName;
}
