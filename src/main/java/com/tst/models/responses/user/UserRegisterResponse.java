package com.tst.models.responses.user;

import com.tst.models.entities.User;
import com.tst.models.enums.EUserRole;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterResponse {
    private String id;
    private String username;
    private EUserRole role;

    public static UserRegisterResponse fromUser(User user) {
        return UserRegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }
}
