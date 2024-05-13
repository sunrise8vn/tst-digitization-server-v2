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
public class WedlockExtractShortDTO {
    @Pattern(regexp = "^\\d*$", message = "ID biểu mẫu phải là số")
    private String id;

    @Pattern(regexp = "^\\d*$", message = "ID dự án phải là số")
    private String projectId;

    private String number;
    private String numberPage;
    private String registrationDate;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String confirmerFullName;

    @Pattern(regexp = "^\\d*$", message = "Giới tính của người xác nhận phải là số")
    private String confirmerGender;

    private String confirmerBirthday;
    private String confirmerNation;
    private String confirmerNationality;
    private String confirmerOtherNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người xác nhận phải là số")
    private String confirmerResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người xác nhận phải là số")
    private String confirmerIdentificationType;

    private String confirmerOtherDocument;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người xác nhận phải là số")
    private String confirmerIdentificationNumber;

    private String confirmerIdentificationIssuanceDate;
    private String confirmerPeriodResidenceFrom;
    private String confirmerPeriodResidenceTo;
    private String petitionerFullName;
    private String petitionerRelationship;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationNumber;

    private String petitionerIdentificationIssuanceDate;
}
