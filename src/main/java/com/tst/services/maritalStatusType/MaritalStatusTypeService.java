package com.tst.services.maritalStatusType;

import com.tst.models.entities.MaritalStatusType;
import com.tst.repositories.MaritalStatusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MaritalStatusTypeService implements IMaritalStatusTypeService {

    private final MaritalStatusTypeRepository maritalStatusTypeRepository;


    @Override
    public Optional<MaritalStatusType> findById(Long id) {
        return maritalStatusTypeRepository.findById(id);
    }

    @Override
    public void delete(MaritalStatusType maritalStatusType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
