package com.tst.repositories;

import com.tst.models.entities.locationRegion.LocationNation;
import com.tst.models.responses.locationRegion.LocationNationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationNationRepository extends JpaRepository<LocationNation, Long> {

    @Query("SELECT NEW com.tst.models.responses.locationRegion.LocationNationResponse (" +
                "lcn.id, " +
                "lcn.name" +
            ") " +
            "FROM LocationNation AS lcn"
    )
    List<LocationNationResponse> findAllLocationNationResponse();
}
