package com.tst.models.responses.extractShort;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.responses.typeList.*;
import lombok.*;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeathExtractShortResponse {
    private Long id;
    private String folderPath;
    private String fileName;
    private String number;
    private String numberPage;
    private String registrationDate;
    private String registrationType;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String deadManFullName;
    private String deadManGender;
    private String deadManBirthday;
    private String deadManNation;
    private String deadManNationality;
    private String deadManResidenceType;
    private String deadManIdentificationType;
    private String deadManIdentificationNumber;
    private String deadManIdentificationIssuanceDate;
    private String deadManDeadDate;
    private String deadManDeadTime;
    private String deathNoticeType;
    private String deathNoticeNumber;
    private String deathNoticeDate;
    private String petitionerFullName;
    private String petitionerRelationship;
    private String petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
