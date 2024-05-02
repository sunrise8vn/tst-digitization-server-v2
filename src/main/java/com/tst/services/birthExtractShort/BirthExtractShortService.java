package com.tst.services.birthExtractShort;

import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.repositories.extractShort.BirthExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BirthExtractShortService implements IBirthExtractShortService {

    private final BirthExtractShortRepository birthExtractShortRepository;


    @Override
    public Optional<BirthExtractShort> findById(Long id) {
        return birthExtractShortRepository.findById(id);
    }

    @Override
    public void delete(BirthExtractShort birthExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
