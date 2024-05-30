package com.tst.models.responses.extractShort;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MarryExtractShortResponse {
    private Long id;
    private String folderPath;
    private String fileName;
    private String number;
    private String numberPage;
    private String registrationDate;
    private String registrationType;
    private String maritalRelationshipEstablishmentDate;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String maritalStatus;
    private String husbandFullName;
    private String husbandBirthday;
    private String husbandNation;
    private String husbandNationality;
    private String husbandResidenceType;
    private String husbandIdentificationType;
    private String husbandIdentificationNumber;
    private String husbandIdentificationIssuanceDate;
    private String wifeFullName;
    private String wifeBirthday;
    private String wifeNation;
    private String wifeNationality;
    private String wifeResidenceType;
    private String wifeIdentificationType;
    private String wifeIdentificationNumber;
    private String wifeIdentificationIssuanceDate;
}
