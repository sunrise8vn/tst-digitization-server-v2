package com.tst.services.maritalStatusType;

import com.tst.models.entities.MaritalStatusType;
import com.tst.models.responses.typeList.MaritalStatusResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IMaritalStatusTypeService extends IGeneralService<MaritalStatusType, Long> {

    List<MaritalStatusResponse> findAllMaritalStatusResponse();

}
