package com.tst.services.genderType;

import com.tst.models.entities.GenderType;
import com.tst.models.responses.typeList.GenderTypeResponse;
import com.tst.repositories.GenderTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenderTypeService implements IGenderTypeService {

    private final GenderTypeRepository genderTypeRepository;


    @Override
    public Optional<GenderType> findById(Long id) {
        return genderTypeRepository.findById(id);
    }

    @Override
    public List<GenderTypeResponse> findAllGenderTypeResponse() {
        return genderTypeRepository.findAllGenderTypeResponse();
    }

    @Override
    public void delete(GenderType genderType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
