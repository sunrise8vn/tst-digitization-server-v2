package com.tst.services.accessPointHistory;

import com.tst.models.entities.AccessPointHistory;
import com.tst.repositories.AccessPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccessPointHistoryService implements IAccessPointHistoryService {

    private final AccessPointHistoryRepository accessPointHistoryRepository;


    @Override
    public Optional<AccessPointHistory> findById(Long id) {
        return accessPointHistoryRepository.findById(id);
    }

    @Override
    public void delete(AccessPointHistory accessPointHistory) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
