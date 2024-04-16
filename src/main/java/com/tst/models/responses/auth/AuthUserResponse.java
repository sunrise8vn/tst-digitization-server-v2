package com.tst.models.responses.auth;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthUserResponse {
    private String id;
    private String username;
    private List<String> roles;
}
