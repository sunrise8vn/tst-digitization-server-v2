package com.tst.services.locationProvince;

import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.responses.locationRegion.LocationProvinceResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface ILocationProvinceService extends IGeneralService<LocationProvince, Long> {

    List<LocationProvinceResponse> findAllLocationProvinceResponse();

}
