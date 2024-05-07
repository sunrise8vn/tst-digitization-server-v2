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
    private String number;
    private String numberPage;
    private String registrationDate;
    private List<RegistrationTypeDetailResponse> registrationType;
    private String signerPosition;
    private String implementer;
    private String note;
    private String deadManFullName;
    private List<GenderTypeResponse> deadManGender;
    private String deadManBirthday;
    private String deadManNation;
    private String deadManNationality;
    private List<ResidenceTypeResponse> deadManResidenceType;
    private List<IdentificationTypeResponse> deadManIdentificationType;
    private String deadManIdentificationNumber;
    private String deadManIdentificationIssuanceDate;
    private String deadManDeadDate;
    private String deadManDeadTime;
    private List<DeathNoticeTypeResponse> deathNoticeType;
    private String deathNoticeNumber;
    private String deathNoticeDate;
    private String petitionerFullName;
    private String petitionerRelationship;
    private List<IdentificationTypeResponse> petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
