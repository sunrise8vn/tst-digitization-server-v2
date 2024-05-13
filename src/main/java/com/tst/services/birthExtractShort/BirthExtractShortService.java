package com.tst.services.birthExtractShort;

import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.extractShort.BirthExtractShortDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractShort.BirthExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BirthExtractShortService implements IBirthExtractShortService {

    private final BirthExtractShortRepository birthExtractShortRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final GenderTypeRepository genderTypeRepository;
    private final BirthCertificateTypeRepository birthCertificateTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<BirthExtractShort> findById(Long id) {
        return birthExtractShortRepository.findById(id);
    }

    @Override
    public Optional<BirthExtractShort> findByIdAndStatus(Long id, EInputStatus status) {
        return birthExtractShortRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<BirthExtractShort> findByIdAndStatusBeforeCompare(Project project, Long id) {
        return birthExtractShortRepository.findByIdAndStatusBeforeCompare(project, id);
    }

    @Override
    public void update(BirthExtractShort birthExtractShort, BirthExtractShortDTO birthExtractShortDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                birthExtractShortDTO.getRegistrationType(),
                ERegistrationType.KS
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại đăng ký không tồn tại");
        });

        genderTypeRepository.findByCode(
                birthExtractShortDTO.getBirtherGender()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giới tính không tồn tại");
        });

        birthCertificateTypeRepository.findByCode(
                birthExtractShortDTO.getBirthCertificateType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại khai sinh không tồn tại");
        });

        residenceTypeRepository.findByCode(
                birthExtractShortDTO.getMomResidenceType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại cư trú của người mẹ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractShortDTO.getMomIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người mẹ không tồn tại");
        });

        residenceTypeRepository.findByCode(
                birthExtractShortDTO.getDadResidenceType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại cư trú của người cha không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractShortDTO.getDadIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người cha không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractShortDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                birthExtractShortDTO,
                birthExtractShort
        );

        birthExtractShort.setStatus(EInputStatus.IMPORTED);

        birthExtractShortRepository.save(birthExtractShort);
    }

    @Override
    public void delete(BirthExtractShort birthExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
