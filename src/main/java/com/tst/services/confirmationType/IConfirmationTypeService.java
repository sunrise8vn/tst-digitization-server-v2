package com.tst.services.confirmationType;

import com.tst.models.entities.ConfirmationType;
import com.tst.models.responses.typeList.ConfirmationTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IConfirmationTypeService extends IGeneralService<ConfirmationType, Long> {

    List<ConfirmationTypeResponse> findAllConfirmationTypeResponse();

}
