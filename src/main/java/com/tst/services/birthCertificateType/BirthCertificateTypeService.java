package com.tst.services.birthCertificateType;

import com.tst.models.entities.BirthCertificateType;
import com.tst.repositories.BirthCertificateTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BirthCertificateTypeService implements IBirthCertificateTypeService {

    private final BirthCertificateTypeRepository birthCertificateTypeRepository;


    @Override
    public Optional<BirthCertificateType> findById(Long id) {
        return birthCertificateTypeRepository.findById(id);
    }

    @Override
    public void delete(BirthCertificateType birthCertificateType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
