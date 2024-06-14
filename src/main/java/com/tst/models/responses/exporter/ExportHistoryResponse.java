package com.tst.models.responses.exporter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.EExportType;
import lombok.*;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExportHistoryResponse {
    private Long id;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String registrationType;
    private String paperSize;
    private String numberBookYear;
    private String numberBook;
    private Integer totalCount;
    private String excelFolderName;
    private String excelName;
    private Long excelSize;
    private String excelSizeType;
    private String zipFolderName;
    private String zipName;
    private Long zipSize;
    private String zipSizeType;
    private EExportStatus status;
    private EExportType exportType;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
