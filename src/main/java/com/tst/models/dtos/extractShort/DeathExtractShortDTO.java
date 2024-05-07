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
public class DeathExtractShortDTO {
    @Pattern(regexp = "^\\d*$", message = "ID phải là số")
    private String id;
    private String number;
    private String numberPage;
    private String registrationDate;

    @Pattern(regexp = "^\\d*$", message = "Loại đăng ký phải là số")
    private String registrationType;

    private String signerPosition;
    private String implementer;
    private String note;
    private String deadManFullName;

    @Pattern(regexp = "^\\d*$", message = "Giới tính của người khai tử phải là số")
    private String deadManGender;

    private String deadManBirthday;
    private String deadManNation;
    private String deadManNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người khai tử phải là số")
    private String deadManResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người khai tử phải là số")
    private String deadManIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người khai tử phải là số")
    private String deadManIdentificationNumber;

    private String deadManIdentificationIssuanceDate;
    private String deadManDeadDate;
    private String deadManDeadTime;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy báo tử phải là số")
    private String deathNoticeType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy báo tử phải là số")
    private String deathNoticeNumber;

    private String deathNoticeDate;
    private String petitionerFullName;
    private String petitionerRelationship;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationNumber;

    private String petitionerIdentificationIssuanceDate;
}
