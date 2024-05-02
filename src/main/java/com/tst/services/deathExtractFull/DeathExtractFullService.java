package com.tst.services.deathExtractFull;

import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.repositories.extractFull.DeathExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeathExtractFullService implements IDeathExtractFullService {

    private final DeathExtractFullRepository deathExtractFullRepository;


    @Override
    public Optional<DeathExtractFull> findById(Long id) {
        return deathExtractFullRepository.findById(id);
    }

    @Override
    public void delete(DeathExtractFull deathExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
