package com.tst.services.user;

import com.tst.models.dtos.user.UserCreateDTO;
import com.tst.models.entities.User;
import com.tst.models.responses.user.UserResponse;
import com.tst.services.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IUserService extends IGeneralService<User, String> {
    User createUser(UserCreateDTO userCreateDTO);

    String login(String username, String password);

    User getUserDetailsFromToken(String token) throws Exception;

    User getUserDetailsFromRefreshToken(String token) throws Exception;

    Page<UserResponse> findAllUserResponse(String keyWord, Pageable pageable);

}
