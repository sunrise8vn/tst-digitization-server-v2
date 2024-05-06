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
    private Integer registrationType;
    private String signerPosition;
    private String implementer;
    private String note;
    private String deadManFullName;
    private Integer deadManGender;
    private String deadManBirthday;
    private String deadManNation;
    private String deadManNationality;
    private Integer deadManResidenceType;
    private Integer deadManIdentificationType;
    private String deadManIdentificationNumber;
    private String deadManIdentificationIssuanceDate;
    private String deadManDeadDate;
    private String deadManDeadTime;
    private Integer deathNoticeType;
    private String deathNoticeNumber;
    private String deathNoticeDate;
    private String petitionerFullName;
    private String petitionerRelationship;
    private Integer petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
