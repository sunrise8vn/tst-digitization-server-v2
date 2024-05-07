package com.tst.models.dtos.extractFull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MarryExtractFullDTO {
    @Pattern(regexp = "^\\d*$", message = "ID phải là số")
    private String id;
    private String number;
    private String numberPage;
    private String registrationDate;

    @Pattern(regexp = "^\\d*$", message = "Loại đăng ký phải là số")
    private String registrationType;

    private String registrationPlace;
    private String signer;
    private String signerPosition;
    private String maritalRelationshipEstablishmentDate;
    private String implementer;
    private String note;

    @Pattern(regexp = "^\\d*$", message = "Tình trạng hôn nhân phải là số")
    private String maritalStatus;

    private String husbandFullName;
    private String husbandBirthday;
    private String husbandNation;
    private String husbandNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người chồng phải là số")
    private String husbandResidenceType;

    private String husbandResidence;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người chồng phải là số")
    private String husbandIdentificationType;

    private String husbandOtherDocument;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người chồng phải là số")
    private String husbandIdentificationNumber;

    private String husbandIdentificationIssuanceDate;
    private String husbandIdentificationIssuancePlace;
    private String wifeFullName;
    private String wifeBirthday;
    private String wifeNation;
    private String wifeNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người vợ phải là số")
    private String wifeResidenceType;

    private String wifeResidence;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người vợ phải là số")
    private String wifeIdentificationType;

    private String wifeOtherDocument;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người chồng phải là số")
    private String wifeIdentificationNumber;

    private String wifeIdentificationIssuanceDate;
    private String wifeIdentificationIssuancePlace;
}
