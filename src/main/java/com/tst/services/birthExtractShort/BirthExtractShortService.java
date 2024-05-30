package com.tst.services.birthExtractShort;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractShort.BirthExtractShortDTO;
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
    public Optional<BirthExtractShort> findByIdForImporter(Long id) {
        return birthExtractShortRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<BirthExtractShort> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return birthExtractShortRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    public void importBeforeCompare(BirthExtractShort birthExtractShort, BirthExtractShortDTO birthExtractShortDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                birthExtractShortDTO.getRegistrationType(),
                ERegistrationType.KS
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        genderTypeRepository.findByCode(
                birthExtractShortDTO.getBirtherGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giới tính không tồn tại");
        });

        birthCertificateTypeRepository.findByCode(
                birthExtractShortDTO.getBirthCertificateType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại khai sinh không tồn tại");
        });

        residenceTypeRepository.findByCode(
                birthExtractShortDTO.getMomResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người mẹ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractShortDTO.getMomIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người mẹ không tồn tại");
        });

        residenceTypeRepository.findByCode(
                birthExtractShortDTO.getDadResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người cha không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractShortDTO.getDadIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người cha không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractShortDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
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
