package com.tst.models.responses.extractFull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.responses.typeList.IdentificationTypeResponse;
import com.tst.models.responses.typeList.MaritalStatusResponse;
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
public class MarryExtractFullResponse {
    private Long id;
    private String number;
    private String numberPage;
    private String registrationDate;
    private List<RegistrationTypeDetailResponse> registrationType;
    private String registrationPlace;
    private String signer;
    private String signerPosition;
    private String maritalRelationshipEstablishmentDate;
    private String implementer;
    private String note;
    private List<MaritalStatusResponse> maritalStatus;
    private String husbandFullName;
    private String husbandBirthday;
    private String husbandNation;
    private String husbandNationality;
    private List<ResidenceTypeResponse> husbandResidenceType;
    private String husbandResidence;
    private List<IdentificationTypeResponse> husbandIdentificationType;
    private String husbandOtherDocument;
    private String husbandIdentificationNumber;
    private String husbandIdentificationIssuanceDate;
    private String husbandIdentificationIssuancePlace;
    private String wifeFullName;
    private String wifeBirthday;
    private String wifeNation;
    private String wifeNationality;
    private List<ResidenceTypeResponse> wifeResidenceType;
    private String wifeResidence;
    private List<IdentificationTypeResponse> wifeIdentificationType;
    private String wifeOtherDocument;
    private String wifeIdentificationNumber;
    private String wifeIdentificationIssuanceDate;
    private String wifeIdentificationIssuancePlace;
}
