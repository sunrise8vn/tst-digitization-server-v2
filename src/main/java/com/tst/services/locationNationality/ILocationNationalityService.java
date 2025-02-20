package com.tst.services.locationNationality;

import com.tst.models.entities.locationRegion.LocationNationality;
import com.tst.models.responses.locationRegion.LocationNationalityResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface ILocationNationalityService extends IGeneralService<LocationNationality, Long> {
    List<LocationNationalityResponse> findAllLocationNationalityResponse();
}
