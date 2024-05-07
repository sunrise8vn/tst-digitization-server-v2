package com.tst.services.registrationTypeDetail;

import com.tst.models.entities.RegistrationTypeDetail;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.typeList.RegistrationTypeDetailResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IRegistrationTypeDetailService extends IGeneralService<RegistrationTypeDetail, Long> {

    List<RegistrationTypeDetailResponse> findAllRegistrationTypeDetailResponse(ERegistrationType eRegistrationType);

}
