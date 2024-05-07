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
public class ParentsChildrenExtractShortDTO {

    @Pattern(regexp = "^\\d*$", message = "ID phải là số")
    private String id;
    private String number;
    private String numberPage;
    private String decisionNo;
    private String registrationDate;

    @Pattern(regexp = "^\\d*$", message = "Loại đăng ký phải là số")
    private String registrationType;

    @Pattern(regexp = "^\\d*$", message = "Loại xác nhận phải là số")
    private String confirmationType;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String parentFullName;
    private String parentBirthday;
    private String parentNation;
    private String parentNationality;
    private String parentOtherNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của cha mẹ phải là số")
    private String parentResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của cha mẹ phải là số")
    private String parentIdentificationType;

    private String parentOtherDocument;
    private String parentIdentificationNumber;
    private String parentIdentificationIssuanceDate;
    private String childFullName;
    private String childBirthday;
    private String childNation;
    private String childNationality;
    private String childOtherNationality;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người con phải là số")
    private String childResidenceType;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người con phải là số")
    private String childIdentificationType;

    private String childOtherDocument;
    private String childIdentificationNumber;
    private String childIdentificationIssuanceDate;
    private String petitionerFullName;
    private String petitionerRecipientRelationship;
    private String petitionerReceiverRelationship;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationType;

    private String petitionerIdentificationNumber;
    private String petitionerIdentificationIssuanceDate;
}
