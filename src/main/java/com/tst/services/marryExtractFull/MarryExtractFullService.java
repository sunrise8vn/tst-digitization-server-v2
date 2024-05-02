package com.tst.services.marryExtractFull;

import com.tst.models.entities.extractFull.MarryExtractFull;
import com.tst.repositories.extractFull.MarryExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MarryExtractFullService implements IMarryExtractFullService {

    private final MarryExtractFullRepository marryExtractFullRepository;


    @Override
    public Optional<MarryExtractFull> findById(Long id) {
        return marryExtractFullRepository.findById(id);
    }

    @Override
    public void delete(MarryExtractFull marryExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
