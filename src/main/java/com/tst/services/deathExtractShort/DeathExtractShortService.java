package com.tst.services.deathExtractShort;

import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.repositories.extractShort.DeathExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeathExtractShortService implements IDeathExtractShortService {

    private final DeathExtractShortRepository deathExtractShortRepository;


    @Override
    public Optional<DeathExtractShort> findById(Long id) {
        return deathExtractShortRepository.findById(id);
    }

    @Override
    public void delete(DeathExtractShort deathExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
