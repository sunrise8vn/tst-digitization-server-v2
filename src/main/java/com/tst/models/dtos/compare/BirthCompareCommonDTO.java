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
    private String registrationType;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String birtherFullName;
    private String birtherGender;
    private String birtherBirthday;
    private String birtherNation;
    private String birtherNationality;
    private String birthCertificateType;
    private String momFullName;
    private String momBirthday;
    private String momNation;
    private String momNationality;
    private String momOtherNationality;
    private String momResidenceType;
    private String momIdentificationType;
    private String momIdentificationNumber;
    private String dadFullName;
    private String dadBirthday;
    private String dadNation;
    private String dadNationality;
    private String dadOtherNationality;
    private String dadResidenceType;
    private String dadIdentificationType;
    private String dadIdentificationNumber;
    private String petitionerFullName;
    private String petitionerRelationship;
    private String petitionerReceiverRelationship;
    private String petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
