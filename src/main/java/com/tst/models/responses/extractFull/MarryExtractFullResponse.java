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
public class MarryExtractFullResponse {
    private Long id;
    private String number;
    private String numberPage;
    private String registrationDate;
    private String registrationType;
    private String registrationPlace;
    private String signer;
    private String signerPosition;
    private String maritalRelationshipEstablishmentDate;
    private String implementer;
    private String note;
    private String wedlock;
    private String husbandFullName;
    private String husbandBirthday;
    private String husbandNation;
    private String husbandNationality;
    private String husbandResidenceType;
    private String husbandResidence;
    private String husbandIdentificationType;
    private String husbandOtherDocument;
    private String husbandIdentificationNumber;
    private String husbandIdentificationIssuanceDate;
    private String husbandIdentificationIssuancePlace;
    private String wifeFullName;
    private String wifeBirthday;
    private String wifeNation;
    private String wifeNationality;
    private String wifeResidenceType;
    private String wifeResidence;
    private String wifeIdentificationType;
    private String wifeOtherDocument;
    private String wifeIdentificationNumber;
    private String wifeIdentificationIssuanceDate;
    private String wifeIdentificationIssuancePlace;
}
