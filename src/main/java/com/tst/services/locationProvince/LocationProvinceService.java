package com.tst.services.locationProvince;

import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.responses.locationRegion.LocationProvinceResponse;
import com.tst.repositories.LocationProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class LocationProvinceService implements ILocationProvinceService {

    private final LocationProvinceRepository locationProvinceRepository;


    @Override
    public List<LocationProvinceResponse> findAllLocationProvinceResponse() {
        List<LocationProvince> provinces = locationProvinceRepository.findAll();

        return provinces.stream()
                .map(LocationProvince::toLocationProvinceResponse)
                .toList();
    }

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
