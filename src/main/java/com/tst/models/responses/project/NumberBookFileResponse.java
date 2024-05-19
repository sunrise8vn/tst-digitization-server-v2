package com.tst.models.responses.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.enums.ERegistrationType;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NumberBookFileResponse {
    private Long id;
    private String provinceName;
    private String districtName;
    private String wardName;
    private ERegistrationType registrationType;
    private EPaperSize paperSize;
    private String folderPath;
    private String fileName;
    private String number;
    private String dayMonthYear;
    private EProjectNumberBookFileStatus status;
}
