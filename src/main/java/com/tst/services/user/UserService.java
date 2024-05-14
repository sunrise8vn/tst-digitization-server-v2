package com.tst.services.user;

import com.tst.components.JwtTokenUtils;
import com.tst.components.LocalizationUtils;
import com.tst.models.dtos.user.UserCreateDTO;
import com.tst.exceptions.*;
import com.tst.models.entities.Role;
import com.tst.models.entities.Token;
import com.tst.models.entities.User;
import com.tst.models.entities.UserInfo;
import com.tst.models.enums.EUserRole;
import com.tst.models.responses.user.UserResponse;
import com.tst.repositories.RoleRepository;
import com.tst.repositories.TokenRepository;
import com.tst.repositories.UserInfoRepository;
import com.tst.repositories.UserRepository;
import com.tst.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;


    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<UserResponse> findAllUserResponse(String keyWord, Pageable pageable) {
        return userRepository.findAllUserResponse(keyWord, pageable);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.VERIFY_TOKEN_EXPIRED));
        }

        String username = jwtTokenUtil.extractUsername(token);
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new Exception("Không tìm thấy tài khoản người dùng");
        }
    }

    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);
        if (tokenOptional.isEmpty()) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_NOT_EXISTS));
        }
        return getUserDetailsFromToken(tokenOptional.get().getToken());
    }

    @Override
    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        String username = userCreateDTO.getUsername();

        if (userRepository.existsByUsername(username)) {
            throw new DataExistsException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_EXISTING));
        }

        Role role = roleRepository.findById(userCreateDTO.getRole_id())
                .orElseThrow(() -> new DataInputException(
                        localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS)));

        if (role.getName().equals(EUserRole.ROLE_ADMIN)) {
            throw new PermissionDenyException("Không được phép đăng ký tài khoản có vai trò " + EUserRole.ROLE_ADMIN.getValue());
        }

        String encodedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User newUser = User.builder()
                .username(userCreateDTO.getUsername())
                .password(encodedPassword)
                .activated(true)
                .build();

        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    @Override
    public String login(
            String username,
            String password
    ) {
        User existingUser = userRepository.findByUsernameOrEmailOrPhoneNumber(username).orElseThrow(() -> {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_USERNAME_PASSWORD));
        });

        if (!existingUser.isActivated()) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                existingUser.getUsername(),
                password
        );

        try {
            //authenticate with Java Spring security
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception ex) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_USERNAME_PASSWORD));
        }

        String fullName = null;

        Optional<UserInfo> userInfo = userInfoRepository.findByUser(existingUser);

        if (userInfo.isPresent()) {
            fullName = userInfo.get().getFullName();
        }

        return jwtTokenUtil.generateToken(existingUser, fullName);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteById(String id) {

    }

}




