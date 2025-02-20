package com.tst.services.birth;

import com.tst.models.entities.Birth;
import com.tst.repositories.BirthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BirthService implements IBirthService {

    private final BirthRepository birthRepository;


    @Override
    public Optional<Birth> findById(Long id) {
        return birthRepository.findById(id);
    }

    @Override
    public void delete(Birth birth) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
