package com.tst.services.locationWard;

import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.responses.locationRegion.LocationWardResponse;
import com.tst.repositories.LocationWardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class LocationWardService implements ILocationWardService {

    private final LocationWardRepository locationWardRepository;


    @Override
    public List<LocationWardResponse> findAllLocationWardResponse(Long districtId) {
        return locationWardRepository.findAllLocationWardResponse(districtId);
    }

    @Override
    public Optional<LocationWard> findById(Long id) {
        return locationWardRepository.findById(id);
    }

    @Override
    public void delete(LocationWard locationWard) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
