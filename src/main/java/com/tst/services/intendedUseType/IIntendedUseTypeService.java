package com.tst.services.intendedUseType;

import com.tst.models.entities.IntendedUseType;
import com.tst.models.responses.typeList.IntendedUseTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IIntendedUseTypeService extends IGeneralService<IntendedUseType, Long> {

    List<IntendedUseTypeResponse> findAllIntendedUseTypeResponse();

}
