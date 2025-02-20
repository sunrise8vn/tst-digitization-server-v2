package com.tst.services.role;

import com.tst.models.entities.Role;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IRoleService extends IGeneralService<Role, Long> {

    Optional<Role> findByCodeNumber(Integer codeNumber);
}
