package com.tst.services.locationProvince;

import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.repositories.LocationProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationProvinceService implements ILocationProvinceService {

    private final LocationProvinceRepository locationProvinceRepository;


    @Override
    public Optional<LocationProvince> findById(Long id) {
        return locationProvinceRepository.findById(id);
    }

    @Override
    public void delete(LocationProvince locationProvince) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
