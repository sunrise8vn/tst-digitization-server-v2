package com.tst.services.locationWard;

import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.repositories.LocationWardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationWardService implements ILocationWardService {

    private final LocationWardRepository locationWardRepository;


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
