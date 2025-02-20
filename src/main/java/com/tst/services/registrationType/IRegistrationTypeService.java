package com.tst.services.registrationType;

import com.tst.models.entities.RegistrationType;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.typeList.RegistrationTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IRegistrationTypeService extends IGeneralService<RegistrationType, Long> {

    Optional<RegistrationType> findByCode(ERegistrationType code);

    List<RegistrationTypeResponse> findAllRegistrationTypeResponse();
}
