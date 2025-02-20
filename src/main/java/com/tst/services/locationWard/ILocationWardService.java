package com.tst.services.locationWard;

import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.responses.locationRegion.LocationWardResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface ILocationWardService extends IGeneralService<LocationWard, Long> {

    List<LocationWardResponse> findAllLocationWardResponse(Long districtId);
}
