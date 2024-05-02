package com.tst.services.wedlockExtractShort;

import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.repositories.extractShort.WedlockExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WedlockExtractShortService implements IWedlockExtractShortService {

    private final WedlockExtractShortRepository wedlockExtractShortRepository;


    @Override
    public Optional<WedlockExtractShort> findById(Long id) {
        return wedlockExtractShortRepository.findById(id);
    }

    @Override
    public void delete(WedlockExtractShort wedlockExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
