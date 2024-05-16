package com.tst.services.birthExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.BirthExtractFullDTO;
import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.BirthExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BirthExtractFullService implements IBirthExtractFullService {

    private final BirthExtractFullRepository birthExtractFullRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final GenderTypeRepository genderTypeRepository;
    private final BirthCertificateTypeRepository birthCertificateTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<BirthExtractFull> findById(Long id) {
        return birthExtractFullRepository.findById(id);
    }

    @Override
    public Optional<BirthExtractFull> findByIdAndStatus(Long id, EInputStatus status) {
        return birthExtractFullRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<BirthExtractFull> findByIdAndStatusBeforeCompare(Long id) {
        return birthExtractFullRepository.findByIdAndStatusBeforeCompare(id);
    }

    @Override
    public Optional<BirthExtractFull> findNextIdAndStatusBeforeCompare(Long projectId, String userId, Long id, String tableName) {
        return birthExtractFullRepository.findNextIdAndStatusBeforeCompare(projectId, userId, id, tableName);
    }

    @Override
    public void importBeforeCompare(BirthExtractFull birthExtractFull, BirthExtractFullDTO birthExtractFullDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                birthExtractFullDTO.getRegistrationType(),
                ERegistrationType.KS
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        genderTypeRepository.findByCode(
                birthExtractFullDTO.getBirtherGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giới tính không tồn tại");
        });

        birthCertificateTypeRepository.findByCode(
                birthExtractFullDTO.getBirthCertificateType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại khai sinh không tồn tại");
        });

        residenceTypeRepository.findByCode(
                birthExtractFullDTO.getMomResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người mẹ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractFullDTO.getMomIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người mẹ không tồn tại");
        });

        residenceTypeRepository.findByCode(
                birthExtractFullDTO.getDadResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người cha không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractFullDTO.getDadIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người cha không tồn tại");
        });

        identificationTypeRepository.findByCode(
                birthExtractFullDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                birthExtractFullDTO,
                birthExtractFull
        );

        birthExtractFull.setStatus(EInputStatus.IMPORTED);

        birthExtractFullRepository.save(birthExtractFull);
    }

    @Override
    public void delete(BirthExtractFull birthExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
