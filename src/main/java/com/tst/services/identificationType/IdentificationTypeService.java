package com.tst.services.identificationType;

import com.tst.models.entities.IdentificationType;
import com.tst.repositories.IdentificationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IdentificationTypeService implements IIdentificationTypeService {

    private final IdentificationTypeRepository identificationTypeRepository;


    @Override
    public Optional<IdentificationType> findById(Long id) {
        return identificationTypeRepository.findById(id);
    }

    @Override
    public void delete(IdentificationType identificationType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
