package com.tst.models.dtos.compare;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MarryCompareCommonDTO {
    private String number;
    private String numberPage;
    private String registrationDate;
    private Integer registrationType;
    private String maritalRelationshipEstablishmentDate;
    private String implementer;
    private String note;
    private String wedlock;
    private String husbandBirthday;
    private String husbandNation;
    private String husbandNationality;
    private Integer husbandResidenceType;
    private Integer husbandIdentificationType;
    private String husbandOtherDocument;
    private String husbandIdentificationNumber;
    private String husbandIdentificationIssuanceDate;
    private String wifeFullName;
    private String wifeBirthday;
    private String wifeNation;
    private String wifeNationality;
    private Integer wifeResidenceType;
    private Integer wifeIdentificationType;
    private String wifeIdentificationNumber;
    private String wifeIdentificationIssuanceDate;
}
