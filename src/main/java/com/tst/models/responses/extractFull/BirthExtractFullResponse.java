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
public class BirthExtractFullResponse {
    private Long id;
    private String number;
    private String numberPage;
    private String registrationDate;
    private String registrationType;
    private String registrationPlace;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String birtherFullName;
    private String birtherGender;
    private String birtherBirthday;
    private String birtherBirthPlace;
    private String birtherBirthPlaceAdministrativeUnit;
    private String birtherHomeTown;
    private String birtherNation;
    private String birtherNationality;
    private String birthCertificateType;
    private String momFullName;
    private String momBirthday;
    private String momNation;
    private String momNationality;
    private String momOtherNationality;
    private String momResidenceType;
    private String momResidence;
    private String momIdentificationType;
    private String momIdentificationNumber;
    private String dadFullName;
    private String dadBirthday;
    private String dadNation;
    private String dadNationality;
    private String dadOtherNationality;
    private String dadResidenceType;
    private String dadResidence;
    private String dadIdentificationType;
    private String dadIdentificationNumber;
    private String petitionerFullName;
    private String petitionerRelationship;
    private String petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
    private String petitionerIdentificationIssuancePlace;
}
