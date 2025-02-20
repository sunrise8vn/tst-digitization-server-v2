package com.tst.services.registrationTypeDetail;

import com.tst.models.entities.RegistrationTypeDetail;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.typeList.RegistrationTypeDetailResponse;
import com.tst.repositories.RegistrationTypeDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegistrationTypeDetailService implements IRegistrationTypeDetailService {

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;


    @Override
    public Optional<RegistrationTypeDetail> findById(Long id) {
        return registrationTypeDetailRepository.findById(id);
    }

    @Override
    public List<RegistrationTypeDetailResponse> findAllRegistrationTypeDetailResponse(ERegistrationType eRegistrationType) {
        return registrationTypeDetailRepository.findAllRegistrationTypeDetailResponse(eRegistrationType);
    }

    @Override
    public void delete(RegistrationTypeDetail registrationTypeDetail) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
