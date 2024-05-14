package com.tst.repositories;

import com.tst.models.entities.User;
import com.tst.models.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUser(User user);
}
