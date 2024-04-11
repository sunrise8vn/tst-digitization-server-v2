package com.tst.services.user;

import com.tst.components.JwtTokenUtils;
import com.tst.components.LocalizationUtils;
import com.tst.models.dtos.UserDTO;
import com.tst.exceptions.*;
import com.tst.models.entities.Role;
import com.tst.models.entities.Token;
import com.tst.models.entities.User;
import com.tst.models.enums.EUserRole;
import com.tst.models.responses.user.UserResponse;
import com.tst.repositories.RoleRepository;
import com.tst.repositories.TokenRepository;
import com.tst.repositories.UserRepository;
import com.tst.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;


    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Page<UserResponse> findAllUserResponse(String keyWord, Pageable pageable) {
        return userRepository.findAllUserResponse(keyWord, pageable);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new UnauthorizedException("Token đã hết hạn");
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
            throw new UnauthorizedException("Token không tồn tại");
        }
        return getUserDetailsFromToken(tokenOptional.get().getToken());
    }

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) {
        String username = userDTO.getUsername();

        if (userRepository.existsByUsername(username)) {
            throw new DataExistsException("Tên tài khoản đã tồn tại");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataInputException(
                        localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS)));

        if (role.getName().equals(EUserRole.ROLE_ADMIN)) {
            throw new PermissionDenyException("Không được phép đăng ký tài khoản có vai trò Admin");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User newUser = User.builder()
                .username(userDTO.getUsername())
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
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_USERNAME_PASSWORD));
        }

        User existingUser = optionalUser.get();

        if (!optionalUser.get().isActivated()) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                password
        );

        try {
            //authenticate with Java Spring security
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception ex) {
            throw new UnauthorizedException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_USERNAME_PASSWORD));
        }

        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteById(String id) {

    }

}




