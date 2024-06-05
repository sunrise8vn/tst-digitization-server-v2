package com.tst.services.locationNationality;

import com.tst.models.entities.locationRegion.LocationNationality;
import com.tst.models.responses.locationRegion.LocationNationalityResponse;
import com.tst.repositories.LocationNationalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationNationalityService implements ILocationNationalityService {

    private final LocationNationalityRepository locationNationalityRepository;

    @Override
    public Optional<LocationNationality> findById(Long id) {
        return locationNationalityRepository.findById(id);
    }

    @Override
    public List<LocationNationalityResponse> findAllLocationNationalityResponse() {
        return locationNationalityRepository.findAllLocationNationalityResponse();
    }

    @Override
    public void delete(LocationNationality locationNationality) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
