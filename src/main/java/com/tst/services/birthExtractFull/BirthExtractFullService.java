package com.tst.services.birthExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.BirthExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.BirthExtractFullRepository;
import com.tst.repositories.extractShort.BirthExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BirthExtractFullService implements IBirthExtractFullService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final BirthExtractFullRepository birthExtractFullRepository;
    private final BirthExtractShortRepository birthExtractShortRepository;

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
    public Optional<BirthExtractFull> findByIdForImporter(Long id) {
        return birthExtractFullRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<BirthExtractFull> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return birthExtractFullRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    public Optional<BirthExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return birthExtractFullRepository.findNextIdByStatusForChecked(project, status, id);
    }

    @Override
    public Optional<BirthExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return birthExtractFullRepository.findPrevIdByStatusForChecked(project, status, id);
    }

    @Override
    @Transactional
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

        if (birthExtractFull.getStatus() == EInputStatus.NOT_MATCHING
                || birthExtractFull.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractFullError(birthExtractFull.getAccessPoint(), birthExtractFull.getImporter());
            accessPointRepository.minusCountExtractFullError(birthExtractFull.getAccessPoint());
        }

        birthExtractFull.setStatus(EInputStatus.IMPORTED);

        birthExtractFullRepository.save(birthExtractFull);
    }

    @Override
    @Transactional
    public void verifyCheckedMatch(BirthExtractFull birthExtractFull) {
        birthExtractFull.setStatus(EInputStatus.CHECKED_MATCHING);
        birthExtractFullRepository.save(birthExtractFull);

        BirthExtractShort birthExtractShort = birthExtractShortRepository.getByProjectNumberBookFile(
                birthExtractFull.getProjectNumberBookFile()
        );
        birthExtractShort.setStatus(EInputStatus.CHECKED_MATCHING);
        birthExtractShortRepository.save(birthExtractShort);
    }

    @Override
    public void verifyCheckedNotMatch(BirthExtractFull birthExtractFull) {
        birthExtractFull.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        birthExtractFullRepository.save(birthExtractFull);

        BirthExtractShort birthExtractShort = birthExtractShortRepository.getByProjectNumberBookFile(
                birthExtractFull.getProjectNumberBookFile()
        );
        birthExtractShort.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        birthExtractShortRepository.save(birthExtractShort);
    }

    @Override
    public void delete(BirthExtractFull birthExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
