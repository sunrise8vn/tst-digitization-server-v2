package com.tst.services.marryExtractShort;

import com.tst.models.entities.extractShort.MarryExtractShort;
import com.tst.repositories.extractShort.MarryExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MarryExtractShortService implements IMarryExtractShortService {

    private final MarryExtractShortRepository marryExtractShortRepository;


    @Override
    public Optional<MarryExtractShort> findById(Long id) {
        return marryExtractShortRepository.findById(id);
    }

    @Override
    public void delete(MarryExtractShort marryExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
