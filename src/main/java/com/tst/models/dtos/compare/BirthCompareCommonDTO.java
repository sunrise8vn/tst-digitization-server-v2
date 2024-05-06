package com.tst.models.dtos.compare;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BirthCompareCommonDTO {
    private String number;
    private String numberPage;
    private String registrationDate;
    private Integer registrationType;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String birtherFullName;
    private Integer birtherGender;
    private String birtherBirthday;
    private String birtherNation;
    private String birtherNationality;
    private Integer birthCertificateType;
    private String momFullName;
    private String momBirthday;
    private String momNation;
    private String momNationality;
    private String momOtherNationality;
    private Integer momResidenceType;
    private Integer momIdentificationType;
    private String momIdentificationNumber;
    private String dadFullName;
    private String dadBirthday;
    private String dadNation;
    private String dadNationality;
    private String dadOtherNationality;
    private Integer dadResidenceType;
    private Integer dadIdentificationType;
    private String dadIdentificationNumber;
    private String petitionerFullName;
    private String petitionerRelationship;
    private String petitionerReceiverRelationship;
    private Integer petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
