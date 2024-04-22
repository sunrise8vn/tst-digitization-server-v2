package com.tst.services.death;

import com.tst.models.entities.Death;
import com.tst.repositories.DeathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeathService implements IDeathService {

    private final DeathRepository deathRepository;


    @Override
    public Optional<Death> findById(Long id) {
        return deathRepository.findById(id);
    }

    @Override
    public void delete(Death death) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
