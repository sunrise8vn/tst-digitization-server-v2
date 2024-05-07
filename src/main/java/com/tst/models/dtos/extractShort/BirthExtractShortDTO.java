package com.tst.models.dtos.extractShort;

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
public class BirthExtractShortDTO {
    @Pattern(regexp = "^\\d*$", message = "ID phải là số")
    private String id;
    private String number;
    private String numberPage;
    private String registrationDate;

    @Pattern(regexp = "^\\d*$", message = "Loại đăng ký phải là số")
    private String registrationType;

    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String birtherFullName;

    @Pattern(regexp = "^\\d*$", message = "Giới tính phải là số")
    private String birtherGender;

    private String birtherBirthday;
    private String birtherNation;
    private String birtherNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại khai sinh phải là số")
    private String birthCertificateType;

    private String momFullName;
    private String momBirthday;
    private String momNation;
    private String momNationality;
    private String momOtherNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người mẹ phải là số")
    private String momResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người mẹ phải là số")
    private String momIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người mẹ phải là số")
    private String momIdentificationNumber;

    private String dadFullName;
    private String dadBirthday;
    private String dadNation;
    private String dadNationality;
    private String dadOtherNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người cha phải là số")
    private String dadResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người cha phải là số")
    private String dadIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người cha phải là số")
    private String dadIdentificationNumber;

    private String petitionerFullName;
    private String petitionerRelationship;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationNumber;

    private String petitionerIdentificationIssuanceDate;
}
