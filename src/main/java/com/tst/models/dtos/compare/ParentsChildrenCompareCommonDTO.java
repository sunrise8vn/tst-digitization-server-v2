package com.tst.models.dtos.compare;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParentsChildrenCompareCommonDTO {
    private String number;
    private String numberPage;
    private String decisionNo;
    private String registrationDate;
    private String registrationType;
    private String confirmationType;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String parentFullName;
    private String parentBirthday;
    private String parentNation;
    private String parentNationality;
    private String parentOtherNationality;
    private String parentResidenceType;
    private String parentIdentificationType;
    private String parentOtherDocument;
    private String parentIdentificationNumber;
    private String parentIdentificationIssuanceDate;
    private String childFullName;
    private String childBirthday;
    private String childNation;
    private String childNationality;
    private String childOtherNationality;
    private String childResidenceType;
    private String childIdentificationType;
    private String childOtherDocument;
    private String childIdentificationNumber;
    private String childIdentificationIssuanceDate;
    private String petitionerFullName;
    private String petitionerRecipientRelationship;
    private String petitionerReceiverRelationship;
    private String petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;

}
