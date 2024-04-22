package com.tst.repositories;

import com.tst.models.entities.locationRegion.LocationWard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationWardRepository extends JpaRepository<LocationWard, Long> {
}
