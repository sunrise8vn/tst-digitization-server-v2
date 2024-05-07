package com.tst.services.residenceType;

import com.tst.models.entities.ResidenceType;
import com.tst.models.responses.typeList.ResidenceTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IResidenceTypeService extends IGeneralService<ResidenceType, Long> {

    List<ResidenceTypeResponse> findAllResidenceTypeResponse();

}
