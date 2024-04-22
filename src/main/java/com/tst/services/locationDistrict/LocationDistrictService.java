package com.tst.services.locationDistrict;

import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.repositories.LocationDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationDistrictService implements ILocationDistrictService {

    private final LocationDistrictRepository locationDistrictRepository;


    @Override
    public Optional<LocationDistrict> findById(Long id) {
        return locationDistrictRepository.findById(id);
    }

    @Override
    public void delete(LocationDistrict locationDistrict) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
