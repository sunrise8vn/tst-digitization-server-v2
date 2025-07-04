package com.tst.services.registrationType;

import com.tst.models.entities.RegistrationType;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.typeList.RegistrationTypeResponse;
import com.tst.repositories.RegistrationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RegistrationTypeService implements IRegistrationTypeService {

    private final RegistrationTypeRepository registrationTypeRepository;


    @Override
    public Optional<RegistrationType> findById(Long id) {
        return registrationTypeRepository.findById(id);
    }

    @Override
    public Optional<RegistrationType> findByCode(ERegistrationType code) {
        return registrationTypeRepository.findByCode(code);
    }

    @Override
    public List<RegistrationTypeResponse> findAllRegistrationTypeResponse() {
        return registrationTypeRepository.findAllRegistrationTypeResponse();
    }

    @Override
    public void delete(RegistrationType registrationType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
