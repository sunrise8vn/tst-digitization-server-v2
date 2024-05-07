package com.tst.models.responses.extractFull;

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
public class ParentsChildrenExtractFullResponse {
    private Long id;
    private String number;
    private String numberPage;
    private String decisionNo;
    private String registrationDate;
    private List<RegistrationTypeDetailResponse> registrationType;
    private List<ConfirmationTypeResponse> confirmationType;
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
    private List<ResidenceTypeResponse> parentResidenceType;
    private String parentResidence;
    private List<IdentificationTypeResponse> parentIdentificationType;
    private String parentOtherDocument;
    private String parentIdentificationNumber;
    private String parentIdentificationIssuanceDate;
    private String parentIdentificationIssuancePlace;
    private String childFullName;
    private String childBirthday;
    private String childNation;
    private String childNationality;
    private String childOtherNationality;
    private String childHomeTown;
    private List<ResidenceTypeResponse> childResidenceType;
    private String childResidence;
    private List<IdentificationTypeResponse> childIdentificationType;
    private String childOtherDocument;
    private String childIdentificationNumber;
    private String childIdentificationIssuanceDate;
    private String childIdentificationIssuancePlace;
    private String petitionerFullName;
    private String petitionerRecipientRelationship;
    private String petitionerReceiverRelationship;
    private List<IdentificationTypeResponse> petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
    private String petitionerIdentificationIssuancePlace;
}
