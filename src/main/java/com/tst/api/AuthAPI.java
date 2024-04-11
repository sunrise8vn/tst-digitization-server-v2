package com.tst.api;

import com.tst.components.LocalizationUtils;
import com.tst.models.dtos.RefreshTokenDTO;
import com.tst.models.dtos.UserDTO;
import com.tst.models.dtos.UserLoginDTO;
import com.tst.models.entities.Token;
import com.tst.models.entities.User;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.user.LoginResponse;
import com.tst.models.responses.user.UserRegisterResponse;
import com.tst.services.token.ITokenService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import com.tst.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    private final AppUtils appUtils;
    private final ITokenService tokenService;


    @PostMapping("/register")
    public ResponseEntity<ResponseObject> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .data(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .build());
        }

        User user = userService.createUser(userDTO);

        return new ResponseEntity<>(
                ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .data(UserRegisterResponse.fromUser(user))
                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                .build(),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
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

        LoginResponse loginResponse = LoginResponse.builder()
                .id(userDetail.getId())
                .username(userDetail.getUsername())
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .data(loginResponse)
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO
    ) throws Exception {
        User userDetail = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(userDetail.getId()).build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .data(loginResponse)
                        .message("Làm mới token thành công")
                        .status(HttpStatus.OK)
                        .build());
    }

}
