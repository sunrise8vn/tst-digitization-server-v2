package com.tst.services.deathExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.DeathExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.DeathExtractFullRepository;
import com.tst.repositories.extractShort.DeathExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeathExtractFullService implements IDeathExtractFullService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final DeathExtractFullRepository deathExtractFullRepository;
    private final DeathExtractShortRepository deathExtractShortRepository;

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
    public Optional<DeathExtractFull> findByIdForImporter(Long id) {
        return deathExtractFullRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<DeathExtractFull> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return deathExtractFullRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    public Optional<DeathExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return deathExtractFullRepository.findNextIdByStatusForChecked(project, status, id);
    }

    @Override
    public Optional<DeathExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return deathExtractFullRepository.findPrevIdByStatusForChecked(project, status, id);
    }

    @Override
    @Transactional
    public void importBeforeCompare(DeathExtractFull deathExtractFull, DeathExtractFullDTO deathExtractFullDTO) {
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

        if (deathExtractFull.getStatus() == EInputStatus.NOT_MATCHING
                || deathExtractFull.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractFullError(deathExtractFull.getAccessPoint(), deathExtractFull.getImporter());
            accessPointRepository.minusCountExtractFullError(deathExtractFull.getAccessPoint());
        }

        deathExtractFull.setStatus(EInputStatus.IMPORTED);

        deathExtractFullRepository.save(deathExtractFull);
    }

    @Override
    @Transactional
    public void verifyCheckedMatch(DeathExtractFull deathExtractFull) {
        deathExtractFull.setStatus(EInputStatus.CHECKED_MATCHING);
        deathExtractFullRepository.save(deathExtractFull);

        DeathExtractShort deathExtractShort = deathExtractShortRepository.getByProjectNumberBookFile(
                deathExtractFull.getProjectNumberBookFile()
        );
        deathExtractShort.setStatus(EInputStatus.CHECKED_MATCHING);
        deathExtractShortRepository.save(deathExtractShort);
    }

    @Override
    public void verifyCheckedNotMatch(DeathExtractFull deathExtractFull) {
        deathExtractFull.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        deathExtractFullRepository.save(deathExtractFull);

        DeathExtractShort deathExtractShort = deathExtractShortRepository.getByProjectNumberBookFile(
                deathExtractFull.getProjectNumberBookFile()
        );
        deathExtractShort.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        deathExtractShortRepository.save(deathExtractShort);
    }

    @Override
    public void delete(DeathExtractFull deathExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
