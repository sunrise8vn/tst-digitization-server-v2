package com.tst.repositories;

import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.responses.locationRegion.LocationWardResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationWardRepository extends JpaRepository<LocationWard, Long> {

    @Query("SELECT NEW com.tst.models.responses.locationRegion.LocationWardResponse (" +
                "lw.id, " +
                "lw.name, " +
                "lw.code, " +
                "lw.divisionType, " +
                "lw.shortCode" +
            ") " +
            "FROM LocationWard AS lw " +
            "WHERE lw.locationDistrict.id = :districtId"
    )
    List<LocationWardResponse> findAllLocationWardResponse(@Param("districtId") Long districtId);
}
