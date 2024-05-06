package com.tst.models.dtos.accessPoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccessPointRevokeDTO {
    @NotBlank(message = "ID điểm truy cập là bắt buộc")
    @Pattern(regexp = "^[1-9]\\d*$", message = "ID điểm truy cập phải là số")
    private String access_point_id;

    @NotBlank(message = "Tổng số phiếu thu hồi là bắt buộc")
    @Pattern(regexp = "^[1-9][0-9]*[02468]$", message = "Tổng số phiếu thu hồi phải là số chẵn lớn hơn 0")
    private String total_count_revoke;
}
