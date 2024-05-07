package com.tst.services.residenceType;

import com.tst.models.entities.ResidenceType;
import com.tst.models.responses.typeList.ResidenceTypeResponse;
import com.tst.repositories.ResidenceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ResidenceTypeService implements IResidenceTypeService {

    private final ResidenceTypeRepository residenceTypeRepository;


    @Override
    public Optional<ResidenceType> findById(Long id) {
        return residenceTypeRepository.findById(id);
    }

    @Override
    public List<ResidenceTypeResponse> findAllResidenceTypeResponse() {
        return residenceTypeRepository.findAllResidenceTypeResponse();
    }

    @Override
    public void delete(ResidenceType residenceType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
