package com.tst.services.deathNoticeType;

import com.tst.models.entities.DeathNoticeType;
import com.tst.models.responses.typeList.DeathNoticeTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IDeathNoticeTypeService extends IGeneralService<DeathNoticeType, Long> {

    List<DeathNoticeTypeResponse> findAllDeathNoticeTypeResponse();

}
