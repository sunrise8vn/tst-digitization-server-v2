package com.tst.services.birthExtractFull;

import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.repositories.extractFull.BirthExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BirthExtractFullService implements IBirthExtractFullService {

    private final BirthExtractFullRepository birthExtractFullRepository;


    @Override
    public Optional<BirthExtractFull> findById(Long id) {
        return birthExtractFullRepository.findById(id);
    }

    @Override
    public void delete(BirthExtractFull birthExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
