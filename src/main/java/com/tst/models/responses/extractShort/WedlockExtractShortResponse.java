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
public class WedlockExtractShortResponse {
    private Long id;
    private String number;
    private String numberPage;
    private String registrationDate;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String confirmerFullName;
    private String confirmerGender;
    private String confirmerBirthday;
    private String confirmerNation;
    private String confirmerNationality;
    private String confirmerOtherNationality;
    private String confirmerResidenceType;
    private String confirmerIdentificationType;
    private String confirmerOtherDocument;
    private String confirmerIdentificationNumber;
    private String confirmerIdentificationIssuanceDate;
    private String confirmerPeriodResidenceFrom;
    private String confirmerPeriodResidenceTo;
    private String petitionerFullName;
    private String petitionerRelationship;
    private String petitionerIdentificationType;
    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
