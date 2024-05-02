package com.tst.services.parentsChildrenExtractShort;

import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.repositories.extractShort.ParentsChildrenExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParentsChildrenExtractShortService implements IParentsChildrenExtractShortService {

    private final ParentsChildrenExtractShortRepository parentsChildrenExtractShortRepository;


    @Override
    public Optional<ParentsChildrenExtractShort> findById(Long id) {
        return parentsChildrenExtractShortRepository.findById(id);
    }

    @Override
    public void delete(ParentsChildrenExtractShort parentsChildrenExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
