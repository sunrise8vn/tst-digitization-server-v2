package com.tst.services.genderType;

import com.tst.models.entities.GenderType;
import com.tst.models.responses.typeList.GenderTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IGenderTypeService extends IGeneralService<GenderType, Long> {

    List<GenderTypeResponse> findAllGenderTypeResponse();

}
