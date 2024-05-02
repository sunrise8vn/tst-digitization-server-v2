package com.tst.services.wedlockExtractFull;

import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.repositories.extractFull.WedlockExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WedlockExtractFullService implements IWedlockExtractFullService {

    private final WedlockExtractFullRepository wedlockExtractFullRepository;


    @Override
    public Optional<WedlockExtractFull> findById(Long id) {
        return wedlockExtractFullRepository.findById(id);
    }

    @Override
    public void delete(WedlockExtractFull wedlockExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
