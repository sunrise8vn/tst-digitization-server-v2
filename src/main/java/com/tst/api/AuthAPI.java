package com.tst.api;

import com.tst.components.LocalizationUtils;
import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.auth.RefreshTokenDTO;
import com.tst.models.dtos.user.UserCreateDTO;
import com.tst.models.dtos.user.UserLoginDTO;
import com.tst.models.entities.*;
import com.tst.models.enums.EUserRole;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.auth.AuthLoginResponse;
import com.tst.models.responses.auth.AuthTokenResponse;
import com.tst.models.responses.auth.AuthUserResponse;
import com.tst.services.project.IProjectService;
import com.tst.services.role.IRoleService;
import com.tst.services.token.ITokenService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import com.tst.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final IProjectService projectService;
    private final IUserService userService;
    private final IRoleService roleService;

    private final LocalizationUtils localizationUtils;
    private final AppUtils appUtils;
    private final ITokenService tokenService;


    @PostMapping("/register")
    public ResponseEntity<ResponseObject> createUser(
            @Validated @RequestBody UserCreateDTO userCreateDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (!userCreateDTO.getPassword().equals(userCreateDTO.getRetypePassword())) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .build());
        }

        Role userRoleCreate = roleService.findByCodeNumber(
                Integer.parseInt(userCreateDTO.getRoleCode())
        ).orElseThrow(() -> new DataInputException("Mã vai trò không tồn tại"));

        Project project = projectService.findById(
                Long.parseLong(userCreateDTO.getProjectId())
        ).orElseThrow(() -> new DataInputException("Mã dự án không tồn tại"));

        User user = userService.getAuthenticatedUser();

        Role userRole = user.getRole();

        Integer roleCodeCreate = userRoleCreate.getCodeNumber();
        Integer roleCode = userRole.getCodeNumber();

        if (roleCodeCreate < roleCode) {
            throw new PermissionDenyException("Bạn không đủ quyền hạn để tạo tài khoản cấp cao");
        }

        userService.createUser(userCreateDTO, userRoleCreate, project);

        return new ResponseEntity<>(
                ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                .status(HttpStatus.CREATED.value())
                .statusText(HttpStatus.CREATED)
                .build(),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Validated @RequestBody UserLoginDTO userLoginDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED))
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        // Kiểm tra thông tin đăng nhập và sinh token
        String token = userService.login(
                userLoginDTO.getUsername(),
                userLoginDTO.getPassword()
        );

        User userDetail = userService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token);

        AuthUserResponse authUserResponse = AuthUserResponse.builder()
                .id(userDetail.getId())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        AuthTokenResponse authTokenResponse = AuthTokenResponse.builder()
                .accessToken(jwtToken.getToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        AuthLoginResponse authLoginResponse = AuthLoginResponse.builder()
                .user(authUserResponse)
                .token(authTokenResponse)
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(authLoginResponse)
                .build());
    }

    @PostMapping("/verify-token")
    public ResponseEntity<ResponseObject> verifyToken() {

        User user = userService.getAuthenticatedUser();
        EUserRole userRole = user.getRole().getName();

        Map<String, String> data = new HashMap<>();
        data.put("role", userRole.getValue());

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.VERIFY_TOKEN_SUCCESSFULLY))
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(data)
                        .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(
            @Validated @RequestBody RefreshTokenDTO refreshTokenDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.VERIFY_TOKEN_FAILED))
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .statusText(HttpStatus.UNAUTHORIZED)
                            .build(),
                    HttpStatus.UNAUTHORIZED
            );
        }

        User userDetail = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefresh_token());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefresh_token(), userDetail);

        AuthTokenResponse authTokenResponse = AuthTokenResponse.builder()
                .accessToken(jwtToken.getToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_SUCCESSFULLY))
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(authTokenResponse)
                        .build());
    }

}
