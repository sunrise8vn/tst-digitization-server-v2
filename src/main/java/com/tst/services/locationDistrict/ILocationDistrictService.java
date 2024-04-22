package com.tst.services.locationDistrict;

import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.responses.locationRegion.LocationDistrictResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface ILocationDistrictService extends IGeneralService<LocationDistrict, Long> {

    List<LocationDistrictResponse> findAllLocationDistrictResponse(Long provinceId);
}
