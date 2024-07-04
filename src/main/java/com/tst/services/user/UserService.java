package com.tst.services.user;

import com.tst.components.JwtTokenUtils;
import com.tst.components.LocalizationUtils;
import com.tst.models.dtos.user.UserCreateDTO;
import com.tst.exceptions.*;
import com.tst.models.entities.*;
import com.tst.models.enums.EUserRole;
import com.tst.models.responses.user.UserResponse;
import com.tst.repositories.*;
import com.tst.utils.AppUtils;
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
    private final ProjectUserRepository projectUserRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;
    private final AppUtils appUtils;


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
    public void createUser(UserCreateDTO userCreateDTO, Role role, Project project) {
        String username = userCreateDTO.getUsername();

        if (userRepository.existsByUsername(username)) {
            throw new DataExistsException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_EXISTING));
        }

        if (role.getName().equals(EUserRole.ROLE_ADMIN)) {
            throw new PermissionDenyException("Không được phép đăng ký tài khoản có vai trò " + EUserRole.ROLE_ADMIN.getValue());
        }

        String encodedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User newUser = User.builder()
                .username(userCreateDTO.getUsername())
                .password(encodedPassword)
                .role(role)
                .activated(true)
                .build();
        userRepository.save(newUser);

        ProjectUser projectUser = new ProjectUser()
                .setUser(newUser)
                .setProject(project);
        projectUserRepository.save(projectUser);

        if (userCreateDTO.getFullName() != null && userCreateDTO.getPhoneNumber() != null) {
            if (userCreateDTO.getPhoneNumber().length() > 10
                    || !appUtils.isValidPhoneNumber(userCreateDTO.getPhoneNumber())
            ) {
                throw new DataExistsException("Số điện thoại không hợp lệ");
            }
            else {
                Optional<UserInfo> userInfoOptional = userInfoRepository.findByPhoneNumber(userCreateDTO.getPhoneNumber());

                if (userInfoOptional.isPresent()) {
                    throw new DataInputException("Số điện thoại này đã được sử dụng");
                }

                UserInfo userInfo = new UserInfo()
                        .setUser(newUser)
                        .setEmail(userCreateDTO.getUsername())
                        .setFullName(userCreateDTO.getFullName())
                        .setPhoneNumber(userCreateDTO.getPhoneNumber());
                userInfoRepository.save(userInfo);
            }
        }
    }

    @Override
    public String login(
            String username,
            String password
    ) {
        User existingUser = userRepository.findByUsernameOrEmailOrPhoneNumber(
                username
        ).orElseThrow(() -> new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_USERNAME_PASSWORD)));

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
    public void changePassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteById(String id) {

    }

}




