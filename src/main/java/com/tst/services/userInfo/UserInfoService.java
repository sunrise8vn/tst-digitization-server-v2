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
    public Optional<UserInfo> findByUser(User user) {
        return userInfoRepository.findByUser(user);
    }

    @Override
    public void updateInfo(User user, UserUpdateInfoDTO userUpdateInfoDTO) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUser(user);

        UserInfo userInfo;

        if (userInfoOptional.isPresent()) {
            Optional<UserInfo> userEmailOptional = userInfoRepository.findByPhoneNumberAndUserIsNot(
                    userUpdateInfoDTO.getEmail(),
                    user
            );

            if (userEmailOptional.isPresent()) {
                throw  new DataExistsException("Email này đã được sử dụng");
            }

            Optional<UserInfo> userPhoneOptional = userInfoRepository.findByPhoneNumberAndUserIsNot(
                    userUpdateInfoDTO.getPhoneNumber(),
                    user
            );

            if (userPhoneOptional.isPresent()) {
                throw  new DataExistsException("Số điện thoại này đã được sử dụng");
            }

            userInfo = userInfoOptional.get();
            userInfo.setFullName(userUpdateInfoDTO.getFullName());
            userInfo.setEmail(userUpdateInfoDTO.getEmail());
            userInfo.setPhoneNumber(userUpdateInfoDTO.getPhoneNumber());
            userInfo.setAddress(userUpdateInfoDTO.getAddress());
        }
        else {
            Optional<UserInfo> userEmailOptional = userInfoRepository.findByEmail(
                    userUpdateInfoDTO.getEmail()
            );

            if (userEmailOptional.isPresent()) {
                throw  new DataExistsException("Email này đã được sử dụng");
            }

            Optional<UserInfo> userPhoneOptional = userInfoRepository.findByPhoneNumber(
                    userUpdateInfoDTO.getPhoneNumber()
            );

            if (userPhoneOptional.isPresent()) {
                throw  new DataExistsException("Số điện thoại này đã được sử dụng");
            }

            userInfo = new UserInfo()
                    .setUser(user)
                    .setFullName(userUpdateInfoDTO.getFullName())
                    .setEmail(userUpdateInfoDTO.getEmail())
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
