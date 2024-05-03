package com.tst.services.accessPoint;

import com.tst.models.entities.AccessPoint;
import com.tst.repositories.AccessPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccessPointService implements IAccessPointService {

    private final AccessPointRepository accessPointRepository;


    @Override
    public Optional<AccessPoint> findById(Long id) {
        return accessPointRepository.findById(id);
    }

    @Override
    public void delete(AccessPoint accessPoint) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
