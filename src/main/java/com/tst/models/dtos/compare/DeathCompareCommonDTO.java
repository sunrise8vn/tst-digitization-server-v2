package com.tst.models.dtos.compare;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeathCompareCommonDTO {
    private String number;
    private String numberPage;
    private String registrationDate;
    private String registrationType;
    private String signerPosition;
    private String implementer;
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
