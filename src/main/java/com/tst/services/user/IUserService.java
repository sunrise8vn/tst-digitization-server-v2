package com.tst.services.user;

import com.tst.models.dtos.user.UserCreateDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.Role;
import com.tst.models.entities.User;
import com.tst.models.responses.user.UserResponse;
import com.tst.services.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface IUserService extends IGeneralService<User, String> {

    User getAuthenticatedUser();

    Optional<User> findByUsername(String username);

    String login(String username, String password);

    User getUserDetailsFromToken(String token) throws Exception;

    User getUserDetailsFromRefreshToken(String token) throws Exception;

    Page<UserResponse> findAllUserResponse(String keyWord, Pageable pageable);

    void createUser(UserCreateDTO userCreateDTO, Role role, Project project);

}
