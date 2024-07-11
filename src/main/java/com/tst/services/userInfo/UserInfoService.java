package com.tst.services.userInfo;

import com.tst.exceptions.DataExistsException;
import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.user.UserChangeInfoDTO;
import com.tst.models.dtos.user.UserUpdateInfoDTO;
import com.tst.models.entities.User;
import com.tst.models.entities.UserInfo;
import com.tst.repositories.UserInfoRepository;
import com.tst.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;


    @Override
    public Optional<UserInfo> findById(Long id) {
        return userInfoRepository.findById(id);
    }

    @Override
    public void updateInfo(User user, UserUpdateInfoDTO userUpdateInfoDTO) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUser(user);

        UserInfo userInfo;

        if (userInfoOptional.isPresent()) {
            Optional<UserInfo> userInfoCurrentOptional = userInfoRepository.findByPhoneNumberAndUserIsNot(
                    userUpdateInfoDTO.getPhoneNumber(),
                    user
            );

            if (userInfoCurrentOptional.isPresent()) {
                throw  new DataExistsException("Số điện thoại này đã được sử dụng");
            }

            userInfo = userInfoOptional.get();
            userInfo.setPhoneNumber(userUpdateInfoDTO.getPhoneNumber());
            userInfo.setFullName(userUpdateInfoDTO.getFullName());
            userInfo.setAddress(userUpdateInfoDTO.getAddress());
        }
        else {
            Optional<UserInfo> userInfoCurrentOptional = userInfoRepository.findByPhoneNumber(
                    userUpdateInfoDTO.getPhoneNumber()
            );

            if (userInfoCurrentOptional.isPresent()) {
                throw  new DataExistsException("Số điện thoại này đã được sử dụng");
            }

            userInfo = new UserInfo()
                    .setUser(user)
                    .setEmail(user.getUsername())
                    .setFullName(userUpdateInfoDTO.getFullName())
                    .setPhoneNumber(userUpdateInfoDTO.getPhoneNumber())
                    .setAddress(userUpdateInfoDTO.getAddress());
        }

        userInfoRepository.save(userInfo);
    }

    @Override
    public void changeInfo(User user, UserChangeInfoDTO userChangeInfoDTO) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUser(user);

        UserInfo userInfo;

        if (userInfoOptional.isPresent()) {
            Optional<UserInfo> userInfoCurrentOptional = userInfoRepository.findByPhoneNumberAndUserIsNot(
                    userChangeInfoDTO.getPhoneNumber(),
                    user
            );

            if (userInfoCurrentOptional.isPresent()) {
                throw  new DataExistsException("Số điện thoại này đã được sử dụng");
            }

            userInfo = userInfoOptional.get();
            userInfo.setPhoneNumber(userChangeInfoDTO.getPhoneNumber());
            userInfo.setFullName(userChangeInfoDTO.getFullName());
            userInfo.setAddress(userChangeInfoDTO.getAddress());
        }
        else {
            Optional<UserInfo> userInfoCurrentOptional = userInfoRepository.findByPhoneNumber(
                    userChangeInfoDTO.getPhoneNumber()
            );

            if (userInfoCurrentOptional.isPresent()) {
                throw  new DataExistsException("Số điện thoại này đã được sử dụng");
            }

            userInfo = new UserInfo()
                    .setUser(user)
                    .setEmail(user.getUsername())
                    .setFullName(userChangeInfoDTO.getFullName())
                    .setPhoneNumber(userChangeInfoDTO.getPhoneNumber())
                    .setAddress(userChangeInfoDTO.getAddress());
        }

        userInfoRepository.save(userInfo);

        userRepository.save(user);
    }

    @Override
    public void delete(UserInfo userInfo) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
