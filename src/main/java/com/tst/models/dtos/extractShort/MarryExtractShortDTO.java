package com.tst.models.dtos.extractShort;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MarryExtractShortDTO {
    @NotBlank(message = "ID biểu mẫu không được rỗng")
    @Pattern(regexp = "^\\d*$", message = "ID biểu mẫu phải là số")
    private String id;

    @NotBlank(message = "ID dự án không được rỗng")
    @Pattern(regexp = "^\\d*$", message = "ID dự án phải là số")
    private String projectId;

    private String number;
    private String numberPage;
    private String registrationDate;

    @Pattern(regexp = "^\\d*$", message = "Loại đăng ký phải là số")
    private String registrationType;

    private String maritalRelationshipEstablishmentDate;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;

    @Pattern(regexp = "^\\d*$", message = "Tình trạng hôn nhân phải là số")
    private String maritalStatus;

    private String husbandBirthday;
    private String husbandNation;
    private String husbandNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người chồng phải là số")
    private String husbandResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người chồng phải là số")
    private String husbandIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người chồng phải là số")
    private String husbandIdentificationNumber;

    private String husbandIdentificationIssuanceDate;
    private String wifeFullName;
    private String wifeBirthday;
    private String wifeNation;
    private String wifeNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người vợ phải là số")
    private String wifeResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người vợ phải là số")
    private String wifeIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người chồng phải là số")
    private String wifeIdentificationNumber;

    private String wifeIdentificationIssuanceDate;
}
