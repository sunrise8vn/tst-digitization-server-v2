package com.tst.services.marry;

import com.tst.models.entities.Marry;
import com.tst.repositories.MarryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MarryService implements IMarryService {

    private final MarryRepository marryRepository;


    @Override
    public Optional<Marry> findById(Long id) {
        return marryRepository.findById(id);
    }

    @Override
    public void delete(Marry marry) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
