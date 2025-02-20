package com.tst.services.parentsChildrenExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.ParentsChildrenExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.ParentsChildrenExtractFullRepository;
import com.tst.repositories.extractShort.ParentsChildrenExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParentsChildrenExtractFullService implements IParentsChildrenExtractFullService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final ParentsChildrenExtractFullRepository parentsChildrenExtractFullRepository;
    private final ParentsChildrenExtractShortRepository parentsChildrenExtractShortRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ConfirmationTypeRepository confirmationTypeRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final GenderTypeRepository genderTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<ParentsChildrenExtractFull> findById(Long id) {
        return parentsChildrenExtractFullRepository.findById(id);
    }

    @Override
    public Optional<ParentsChildrenExtractFull> findByIdAndStatus(Long id, EInputStatus status) {
        return parentsChildrenExtractFullRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<ParentsChildrenExtractFull> findByIdForImporter(Long id) {
        return parentsChildrenExtractFullRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<ParentsChildrenExtractFull> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return parentsChildrenExtractFullRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    public Optional<ParentsChildrenExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return parentsChildrenExtractFullRepository.findNextIdByStatusForChecked(project, status, id);
    }

    @Override
    public Optional<ParentsChildrenExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return parentsChildrenExtractFullRepository.findPrevIdByStatusForChecked(project, status, id);
    }

    @Override
    @Transactional
    public void importBeforeCompare(ParentsChildrenExtractFull parentsChildrenExtractFull, ParentsChildrenExtractFullDTO parentsChildrenExtractFullDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                parentsChildrenExtractFullDTO.getRegistrationType(),
                ERegistrationType.CMC
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        confirmationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getConfirmationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại xác nhận không tồn tại");
        });

        residenceTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getParentResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của cha mẹ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getParentIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của cha mẹ không tồn tại");
        });

        residenceTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getChildResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người con không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getChildIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người con không tồn tại");
        });

        genderTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getChildGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giới tính của người con không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                parentsChildrenExtractFullDTO,
                parentsChildrenExtractFull
        );

        if (parentsChildrenExtractFull.getStatus() == EInputStatus.NOT_MATCHING
                || parentsChildrenExtractFull.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractFullError(parentsChildrenExtractFull.getAccessPoint(), parentsChildrenExtractFull.getImporter());
            accessPointRepository.minusCountExtractFullError(parentsChildrenExtractFull.getAccessPoint());
        }

        parentsChildrenExtractFull.setStatus(EInputStatus.IMPORTED);

        parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);
    }

    @Override
    public void verifyComparedMatch(ParentsChildrenExtractFull parentsChildrenExtractFull, User compareChecker) {
        parentsChildrenExtractFull.setStatus(EInputStatus.CHECKED_MATCHING);
        parentsChildrenExtractFull.setCompareCheckedAt(LocalDateTime.now());
        parentsChildrenExtractFull.setCompareChecker(compareChecker);
        parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortRepository.getByProjectNumberBookFile(
                parentsChildrenExtractFull.getProjectNumberBookFile()
        );
        parentsChildrenExtractShort.setStatus(EInputStatus.CHECKED_MATCHING);
        parentsChildrenExtractShort.setCompareCheckedAt(LocalDateTime.now());
        parentsChildrenExtractShort.setCompareChecker(compareChecker);
        parentsChildrenExtractShortRepository.save(parentsChildrenExtractShort);
    }

    @Override
    public void verifyComparedNotMatch(ParentsChildrenExtractFull parentsChildrenExtractFull, User compareChecker) {
        parentsChildrenExtractFull.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        parentsChildrenExtractFull.setCompareCheckedAt(LocalDateTime.now());
        parentsChildrenExtractFull.setCompareChecker(compareChecker);
        parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortRepository.getByProjectNumberBookFile(
                parentsChildrenExtractFull.getProjectNumberBookFile()
        );
        parentsChildrenExtractShort.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        parentsChildrenExtractShort.setCompareCheckedAt(LocalDateTime.now());
        parentsChildrenExtractShort.setCompareChecker(compareChecker);
        parentsChildrenExtractShortRepository.save(parentsChildrenExtractShort);
    }

    @Override
    @Transactional
    public void verifyCheckedComparedMatch(ParentsChildrenExtractFull parentsChildrenExtractFull, User finalChecker) {
        parentsChildrenExtractFull.setStatus(EInputStatus.FINAL_MATCHING);
        parentsChildrenExtractFull.setFinalCheckedAt(LocalDateTime.now());
        parentsChildrenExtractFull.setFinalChecker(finalChecker);
        parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortRepository.getByProjectNumberBookFile(
                parentsChildrenExtractFull.getProjectNumberBookFile()
        );
        parentsChildrenExtractShort.setStatus(EInputStatus.FINAL_MATCHING);
        parentsChildrenExtractShort.setFinalCheckedAt(LocalDateTime.now());
        parentsChildrenExtractShort.setFinalChecker(finalChecker);
        parentsChildrenExtractShortRepository.save(parentsChildrenExtractShort);
    }

    @Override
    public void verifyCheckedComparedNotMatch(ParentsChildrenExtractFull parentsChildrenExtractFull, User finalChecker) {
        parentsChildrenExtractFull.setStatus(EInputStatus.FINAL_NOT_MATCHING);
        parentsChildrenExtractFull.setFinalCheckedAt(LocalDateTime.now());
        parentsChildrenExtractFull.setFinalChecker(finalChecker);
        parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortRepository.getByProjectNumberBookFile(
                parentsChildrenExtractFull.getProjectNumberBookFile()
        );
        parentsChildrenExtractShort.setStatus(EInputStatus.FINAL_NOT_MATCHING);
        parentsChildrenExtractShort.setFinalCheckedAt(LocalDateTime.now());
        parentsChildrenExtractShort.setFinalChecker(finalChecker);
        parentsChildrenExtractShortRepository.save(parentsChildrenExtractShort);
    }

    @Override
    public void delete(ParentsChildrenExtractFull parentsChildrenExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
