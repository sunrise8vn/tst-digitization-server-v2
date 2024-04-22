package com.tst.services.userInfo;

import com.tst.models.entities.UserInfo;
import com.tst.repositories.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;


    @Override
    public Optional<UserInfo> findById(Long id) {
        return userInfoRepository.findById(id);
    }

    @Override
    public void delete(UserInfo userInfo) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
