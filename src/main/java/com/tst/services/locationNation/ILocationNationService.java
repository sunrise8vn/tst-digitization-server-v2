package com.tst.services.locationNation;

import com.tst.models.entities.locationRegion.LocationNation;
import com.tst.models.responses.locationRegion.LocationNationResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface ILocationNationService extends IGeneralService<LocationNation, Long> {

    List<LocationNationResponse> findAllLocationNationResponse();
}
