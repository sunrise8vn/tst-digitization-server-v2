package com.tst.models.dtos.project;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddUsersToProjectDTO {
    @NotEmpty(message = "Danh sách tài khoản người dùng là bắt buộc")
    private List<String> users;
}
