package com.tst.repositories;

import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.responses.locationRegion.LocationDistrictResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LocationDistrictRepository extends JpaRepository<LocationDistrict, Long> {

    @Query("SELECT NEW com.tst.models.responses.locationRegion.LocationDistrictResponse (" +
                "ld.id, " +
                "ld.name, " +
                "ld.code, " +
                "ld.divisionType, " +
                "ld.shortCode" +
            ") " +
            "FROM LocationDistrict AS ld " +
            "WHERE ld.locationProvince.id = :provinceId"
    )
    List<LocationDistrictResponse> findAllLocationDistrictResponse(@Param("provinceId") Long provinceId);
}
