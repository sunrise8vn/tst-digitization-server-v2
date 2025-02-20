package com.tst.services.wedlock;

import com.tst.models.entities.Wedlock;
import com.tst.repositories.WedlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WedlockService implements IWedlockService {

    private final WedlockRepository wedlockRepository;


    @Override
    public Optional<Wedlock> findById(Long id) {
        return wedlockRepository.findById(id);
    }

    @Override
    public void delete(Wedlock wedlock) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
