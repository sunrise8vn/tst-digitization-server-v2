package com.tst.repositories;

import com.tst.models.entities.locationRegion.LocationNationality;
import com.tst.models.responses.locationRegion.LocationNationalityResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationNationalityRepository extends JpaRepository<LocationNationality, Long> {

    @Query("SELECT NEW com.tst.models.responses.locationRegion.LocationNationalityResponse (" +
                "lcn.id, " +
                "lcn.name" +
            ") " +
            "FROM LocationNationality AS lcn"
    )
    List<LocationNationalityResponse> findAllLocationNationalityResponse();
}
