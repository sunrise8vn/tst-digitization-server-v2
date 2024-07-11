package com.tst.services.userInfo;

import com.tst.models.dtos.user.UserChangeInfoDTO;
import com.tst.models.dtos.user.UserUpdateInfoDTO;
import com.tst.models.entities.User;
import com.tst.models.entities.UserInfo;
import com.tst.services.IGeneralService;

public interface IUserInfoService extends IGeneralService<UserInfo, Long> {

    void updateInfo(User user, UserUpdateInfoDTO userUpdateInfoDTO);

    void changeInfo(User user, UserChangeInfoDTO userChangeInfoDTO);

}
