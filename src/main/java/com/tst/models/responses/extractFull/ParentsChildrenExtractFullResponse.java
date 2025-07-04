package com.tst.models.responses.extractFull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParentsChildrenExtractFullResponse {
    private Long id;
    private String folderPath;
    private String fileName;
    private String number;
    private String numberPage;
    private String decisionNo;
    private String registrationDate;
    private String registrationType;
    private String confirmationType;
    private String registrationPlace;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String parentFullName;
    private String parentBirthday;
    private String parentNation;
    private String parentNationality;
    private String parentOtherNationality;
    private String parentHomeTown;
    private String parentResidenceType;
    private String parentResidence;
    private String parentIdentificationType;
    private String parentOtherDocument;
    private String parentIdentificationNumber;
    private String parentIdentificationIssuanceDate;
    private String parentIdentificationIssuancePlace;
    private String childFullName;
    private String childGender;
    private String childBirthday;
    private String childNation;
    private String childNationality;
    private String childOtherNationality;
    private String childHomeTown;
    private String childResidenceType;
    private String childResidence;
    private String childIdentificationType;
    private String childOtherDocument;
    private String childIdentificationNumber;
    private String childIdentificationIssuanceDate;
    private String childIdentificationIssuancePlace;
    private String petitionerFullName;
    private String petitionerRecipientRelationship;
    private String petitionerReceiverRelationship;
    private String petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
    private String petitionerIdentificationIssuancePlace;
}
