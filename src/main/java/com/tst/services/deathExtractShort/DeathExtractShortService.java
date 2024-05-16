package com.tst.services.deathExtractShort;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractShort.DeathExtractShortDTO;
import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractShort.DeathExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeathExtractShortService implements IDeathExtractShortService {

    private final DeathExtractShortRepository deathExtractShortRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final GenderTypeRepository genderTypeRepository;
    private final DeathNoticeTypeRepository deathNoticeTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<DeathExtractShort> findById(Long id) {
        return deathExtractShortRepository.findById(id);
    }

    @Override
    public Optional<DeathExtractShort> findByIdAndStatus(Long id, EInputStatus status) {
        return deathExtractShortRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<DeathExtractShort> findByIdAndStatusBeforeCompare(Long id) {
        return deathExtractShortRepository.findByIdAndStatusBeforeCompare(id);
    }

    @Override
    public Optional<DeathExtractShort> findNextIdAndStatusBeforeCompare(Long projectId, String userId, Long id, String tableName) {
        return deathExtractShortRepository.findNextIdAndStatusBeforeCompare(projectId, userId, id, tableName);
    }

    @Override
    public void importBeforeCompare(DeathExtractShort deathExtractShort, DeathExtractShortDTO deathExtractShortDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                deathExtractShortDTO.getRegistrationType(),
                ERegistrationType.KT
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        genderTypeRepository.findByCode(
                deathExtractShortDTO.getDeadManGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giới tính của người khai tử không tồn tại");
        });

        residenceTypeRepository.findByCode(
                deathExtractShortDTO.getDeadManResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người khai tử không tồn tại");
        });

        identificationTypeRepository.findByCode(
                deathExtractShortDTO.getDeadManIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người khai tử không tồn tại");
        });

        deathNoticeTypeRepository.findByCode(
                deathExtractShortDTO.getDeathNoticeType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy báo tử không tồn tại");
        });

        identificationTypeRepository.findByCode(
                deathExtractShortDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                deathExtractShortDTO,
                deathExtractShort
        );

        deathExtractShort.setStatus(EInputStatus.IMPORTED);

        deathExtractShortRepository.save(deathExtractShort);
    }

    @Override
    public void delete(DeathExtractShort deathExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
