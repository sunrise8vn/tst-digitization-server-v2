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
    private Integer registrationType;
    private Integer confirmationType;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String parentFullName;
    private String parentBirthday;
    private String parentNation;
    private String parentNationality;
    private String parentOtherNationality;
    private Integer parentResidenceType;
    private Integer parentIdentificationType;
    private String parentOtherDocument;
    private String parentIdentificationNumber;
    private String parentIdentificationIssuanceDate;
    private String childFullName;
    private String childBirthday;
    private String childNation;
    private String childNationality;
    private String childOtherNationality;
    private Integer childResidenceType;
    private Integer childIdentificationType;
    private String childOtherDocument;
    private String childIdentificationNumber;
    private String childIdentificationIssuanceDate;
    private String petitionerFullName;
    private String petitionerRecipientRelationship;
    private String petitionerReceiverRelationship;
    private Integer petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;

}
