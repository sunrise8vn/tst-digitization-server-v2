package com.tst.services.parentsChildrenExtractFull;

import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.repositories.extractFull.ParentsChildrenExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParentsChildrenExtractFullService implements IParentsChildrenExtractFullService {

    private final ParentsChildrenExtractFullRepository parentsChildrenExtractFullRepository;


    @Override
    public Optional<ParentsChildrenExtractFull> findById(Long id) {
        return parentsChildrenExtractFullRepository.findById(id);
    }

    @Override
    public void delete(ParentsChildrenExtractFull parentsChildrenExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
