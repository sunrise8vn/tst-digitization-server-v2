package com.tst.services.locationNation;

import com.tst.models.entities.locationRegion.LocationNation;
import com.tst.models.responses.locationRegion.LocationNationResponse;
import com.tst.repositories.LocationNationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationNationService implements ILocationNationService {

    private final LocationNationRepository locationNationRepository;

    @Override
    public Optional<LocationNation> findById(Long id) {
        return locationNationRepository.findById(id);
    }

    @Override
    public List<LocationNationResponse> findAllLocationNationResponse() {
        return locationNationRepository.findAllLocationNationResponse();
    }

    @Override
    public void delete(LocationNation locationNation) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
