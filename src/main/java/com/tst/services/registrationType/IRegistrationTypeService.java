package com.tst.services.registrationType;

import com.tst.models.entities.RegistrationType;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IRegistrationTypeService extends IGeneralService<RegistrationType, Long> {

    Optional<RegistrationType> findByCode(String code);
}
