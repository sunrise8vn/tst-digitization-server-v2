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
public class WedlockExtractFullDTO {
    @Pattern(regexp = "^\\d*$", message = "ID phải là số")
    private String id;

    private String number;
    private String numberPage;
    private String registrationDate;
    private String issuancePlace;
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

    private String confirmerResidence;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người xác nhận phải là số")
    private String confirmerIdentificationType;

    private String confirmerOtherDocument;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người xác nhận phải là số")
    private String confirmerIdentificationNumber;

    private String confirmerIdentificationIssuanceDate;
    private String confirmerIdentificationIssuancePlace;
    private String confirmerPeriodResidenceAt;
    private String confirmerPeriodResidenceFrom;
    private String confirmerPeriodResidenceTo;
    private String confirmerMaritalStatus;

    @Pattern(regexp = "^\\d*$", message = "Loại mục đích sử dụng của người xác nhận phải là số")
    private String confirmerIntendedUseType;

    private String confirmerIntendedUse;
    private String petitionerFullName;
    private String petitionerRelationship;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationNumber;

    private String petitionerIdentificationIssuanceDate;
    private String petitionerIdentificationIssuancePlace;
}
