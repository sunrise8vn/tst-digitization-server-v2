package com.tst.services.birthCertificateType;

import com.tst.models.entities.BirthCertificateType;
import com.tst.models.responses.typeList.BirthCertificateTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IBirthCertificateTypeService extends IGeneralService<BirthCertificateType, Long> {

    List<BirthCertificateTypeResponse> findAllBirthCertificateTypeResponse();

}
