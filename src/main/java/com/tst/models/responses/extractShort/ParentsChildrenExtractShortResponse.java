package com.tst.models.responses.extractShort;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.responses.typeList.ConfirmationTypeResponse;
import com.tst.models.responses.typeList.IdentificationTypeResponse;
import com.tst.models.responses.typeList.RegistrationTypeDetailResponse;
import com.tst.models.responses.typeList.ResidenceTypeResponse;
import lombok.*;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParentsChildrenExtractShortResponse {
    private Long id;
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
