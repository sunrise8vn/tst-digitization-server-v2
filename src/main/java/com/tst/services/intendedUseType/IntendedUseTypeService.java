package com.tst.services.intendedUseType;

import com.tst.models.entities.IntendedUseType;
import com.tst.repositories.IntendedUseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IntendedUseTypeService implements IIntendedUseTypeService {

    private final IntendedUseTypeRepository intendedUseTypeRepository;


    @Override
    public Optional<IntendedUseType> findById(Long id) {
        return intendedUseTypeRepository.findById(id);
    }

    @Override
    public void delete(IntendedUseType intendedUseType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
