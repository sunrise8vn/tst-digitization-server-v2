package com.tst.services.deathExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.DeathExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.DeathExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeathExtractFullService implements IDeathExtractFullService {

    private final DeathExtractFullRepository deathExtractFullRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final GenderTypeRepository genderTypeRepository;
    private final DeathNoticeTypeRepository deathNoticeTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<DeathExtractFull> findById(Long id) {
        return deathExtractFullRepository.findById(id);
    }

    @Override
    public Optional<DeathExtractFull> findByIdAndStatus(Long id, EInputStatus status) {
        return deathExtractFullRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<DeathExtractFull> findByIdAndStatusBeforeCompare(Project project, Long id) {
        return deathExtractFullRepository.findByIdAndStatusBeforeCompare(project, id);
    }

    @Override
    public void update(DeathExtractFull deathExtractFull, DeathExtractFullDTO deathExtractFullDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                deathExtractFullDTO.getRegistrationType(),
                ERegistrationType.KT
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        genderTypeRepository.findByCode(
                deathExtractFullDTO.getDeadManGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giới tính của người khai tử không tồn tại");
        });

        residenceTypeRepository.findByCode(
                deathExtractFullDTO.getDeadManResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người khai tử không tồn tại");
        });

        identificationTypeRepository.findByCode(
                deathExtractFullDTO.getDeadManIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người khai tử không tồn tại");
        });

        deathNoticeTypeRepository.findByCode(
                deathExtractFullDTO.getDeathNoticeType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy báo tử không tồn tại");
        });

        identificationTypeRepository.findByCode(
                deathExtractFullDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                deathExtractFullDTO,
                deathExtractFull
        );

        deathExtractFull.setStatus(EInputStatus.IMPORTED);

        deathExtractFullRepository.save(deathExtractFull);
    }

    @Override
    public void delete(DeathExtractFull deathExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
