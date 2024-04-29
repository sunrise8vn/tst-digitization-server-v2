package com.tst.services.registrationType;

import com.tst.models.entities.RegistrationType;
import com.tst.repositories.RegistrationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Optional<RegistrationType> findByCode(String code) {
        return registrationTypeRepository.findByCode(code);
    }

    @Override
    public void delete(RegistrationType registrationType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
