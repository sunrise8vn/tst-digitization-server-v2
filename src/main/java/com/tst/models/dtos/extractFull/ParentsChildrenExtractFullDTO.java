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
public class ParentsChildrenExtractFullDTO {
    @Pattern(regexp = "^\\d*$", message = "ID biểu mẫu phải là số")
    private String id;

    @Pattern(regexp = "^\\d*$", message = "ID dự án phải là số")
    private String projectId;

    private String number;
    private String numberPage;

    @Pattern(regexp = "^\\d*$", message = "Số quyết định phải là số")
    private String decisionNo;

    private String registrationDate;

    @Pattern(regexp = "^\\d*$", message = "Loại đăng ký phải là số")
    private String registrationType;

    @Pattern(regexp = "^\\d*$", message = "Loại xác nhận phải là số")
    private String confirmationType;

    private String registrationPlace;
    private String signer;
    private String signerPosition;
    private String implementer;
    private String note;
    private String parentFullName;
    private String parentBirthday;
    private String parentNation;
    private String parentNationality;
    private String parentOtherNationality;
    private String parentHomeTown;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của cha mẹ phải là số")
    private String parentResidenceType;

    private String parentResidence;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của cha mẹ phải là số")
    private String parentIdentificationType;

    private String parentOtherDocument;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của cha mẹ phải là số")
    private String parentIdentificationNumber;

    private String parentIdentificationIssuanceDate;
    private String parentIdentificationIssuancePlace;
    private String childFullName;

    @Pattern(regexp = "^\\d*$", message = "Giới tính của người con phải là số")
    private String childGender;

    private String childBirthday;
    private String childNation;
    private String childNationality;
    private String childOtherNationality;
    private String childHomeTown;

    @Pattern(regexp = "^\\d*$", message = "Loại cư trú của người con phải là số")
    private String childResidenceType;

    private String childResidence;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người con phải là số")
    private String childIdentificationType;

    private String childOtherDocument;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người con phải là số")
    private String childIdentificationNumber;

    private String childIdentificationIssuanceDate;
    private String childIdentificationIssuancePlace;
    private String petitionerFullName;
    private String petitionerRecipientRelationship;
    private String petitionerReceiverRelationship;

    @Pattern(regexp = "^\\d*$", message = "Loại giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationType;

    @Pattern(regexp = "^\\d*$", message = "Số giấy tờ tùy thân của người yêu cầu phải là số")
    private String petitionerIdentificationNumber;

    private String petitionerIdentificationIssuanceDate;
    private String petitionerIdentificationIssuancePlace;
}
