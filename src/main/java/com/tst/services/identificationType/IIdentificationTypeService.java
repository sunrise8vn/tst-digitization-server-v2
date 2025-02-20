package com.tst.services.identificationType;

import com.tst.models.entities.IdentificationType;
import com.tst.models.responses.typeList.IdentificationTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IIdentificationTypeService extends IGeneralService<IdentificationType, Long> {

    List<IdentificationTypeResponse> findAllIdentificationTypeResponse();

}
