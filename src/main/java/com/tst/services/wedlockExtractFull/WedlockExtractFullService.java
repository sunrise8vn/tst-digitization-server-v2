package com.tst.services.wedlockExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.WedlockExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.WedlockExtractFullRepository;
import com.tst.repositories.extractShort.WedlockExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WedlockExtractFullService implements IWedlockExtractFullService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final WedlockExtractFullRepository wedlockExtractFullRepository;
    private final WedlockExtractShortRepository wedlockExtractShortRepository;
    private final GenderTypeRepository genderTypeRepository;
    private final IntendedUseTypeRepository intendedUseTypeRepository;

    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<WedlockExtractFull> findById(Long id) {
        return wedlockExtractFullRepository.findById(id);
    }

    @Override
    public Optional<WedlockExtractFull> findByIdAndStatus(Long id, EInputStatus status) {
        return wedlockExtractFullRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<WedlockExtractFull> findByIdForImporter(Long id) {
        return wedlockExtractFullRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<WedlockExtractFull> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return wedlockExtractFullRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    public Optional<WedlockExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return wedlockExtractFullRepository.findNextIdByStatusForChecked(project, status, id);
    }

    @Override
    public Optional<WedlockExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id) {
        return wedlockExtractFullRepository.findPrevIdByStatusForChecked(project, status, id);
    }

    @Override
    @Transactional
    public void importBeforeCompare(WedlockExtractFull wedlockExtractFull, WedlockExtractFullDTO wedlockExtractFullDTO) {
        genderTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Giới tính của người xác nhận không tồn tại");
        });

        residenceTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người xác nhận không tồn tại");
        });

        intendedUseTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerIntendedUseType()
        ).orElseThrow(() -> {
           throw new DataInputException("Loại mục đích sử dụng của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractFullDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                wedlockExtractFullDTO,
                wedlockExtractFull
        );

        if (wedlockExtractFull.getStatus() == EInputStatus.NOT_MATCHING
                || wedlockExtractFull.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractFullError(wedlockExtractFull.getAccessPoint(), wedlockExtractFull.getImporter());
            accessPointRepository.minusCountExtractFullError(wedlockExtractFull.getAccessPoint());
        }

        wedlockExtractFull.setStatus(EInputStatus.IMPORTED);

        wedlockExtractFullRepository.save(wedlockExtractFull);
    }

    @Override
    public void verifyComparedMatch(WedlockExtractFull wedlockExtractFull, User compareChecker) {
        wedlockExtractFull.setStatus(EInputStatus.CHECKED_MATCHING);
        wedlockExtractFull.setCompareCheckedAt(LocalDateTime.now());
        wedlockExtractFull.setCompareChecker(compareChecker);
        wedlockExtractFullRepository.save(wedlockExtractFull);

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortRepository.getByProjectNumberBookFile(
                wedlockExtractFull.getProjectNumberBookFile()
        );
        wedlockExtractShort.setStatus(EInputStatus.CHECKED_MATCHING);
        wedlockExtractShort.setCompareCheckedAt(LocalDateTime.now());
        wedlockExtractShort.setCompareChecker(compareChecker);
        wedlockExtractShortRepository.save(wedlockExtractShort);
    }

    @Override
    public void verifyComparedNotMatch(WedlockExtractFull wedlockExtractFull, User compareChecker) {
        wedlockExtractFull.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        wedlockExtractFull.setCompareCheckedAt(LocalDateTime.now());
        wedlockExtractFull.setCompareChecker(compareChecker);
        wedlockExtractFullRepository.save(wedlockExtractFull);

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortRepository.getByProjectNumberBookFile(
                wedlockExtractFull.getProjectNumberBookFile()
        );
        wedlockExtractShort.setStatus(EInputStatus.CHECKED_NOT_MATCHING);
        wedlockExtractShort.setCompareCheckedAt(LocalDateTime.now());
        wedlockExtractShort.setCompareChecker(compareChecker);
        wedlockExtractShortRepository.save(wedlockExtractShort);
    }

    @Override
    public void verifyCheckedComparedMatch(WedlockExtractFull wedlockExtractFull, User finalChecker) {
        wedlockExtractFull.setStatus(EInputStatus.FINAL_MATCHING);
        wedlockExtractFull.setFinalCheckedAt(LocalDateTime.now());
        wedlockExtractFull.setFinalChecker(finalChecker);
        wedlockExtractFullRepository.save(wedlockExtractFull);

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortRepository.getByProjectNumberBookFile(
                wedlockExtractFull.getProjectNumberBookFile()
        );
        wedlockExtractShort.setStatus(EInputStatus.FINAL_MATCHING);
        wedlockExtractShort.setFinalCheckedAt(LocalDateTime.now());
        wedlockExtractShort.setFinalChecker(finalChecker);
        wedlockExtractShortRepository.save(wedlockExtractShort);
    }

    @Override
    public void verifyCheckedComparedNotMatch(WedlockExtractFull wedlockExtractFull, User finalChecker) {
        wedlockExtractFull.setStatus(EInputStatus.FINAL_NOT_MATCHING);
        wedlockExtractFull.setFinalCheckedAt(LocalDateTime.now());
        wedlockExtractFull.setFinalChecker(finalChecker);
        wedlockExtractFullRepository.save(wedlockExtractFull);

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortRepository.getByProjectNumberBookFile(
                wedlockExtractFull.getProjectNumberBookFile()
        );
        wedlockExtractShort.setStatus(EInputStatus.FINAL_NOT_MATCHING);
        wedlockExtractShort.setFinalCheckedAt(LocalDateTime.now());
        wedlockExtractShort.setFinalChecker(finalChecker);
        wedlockExtractShortRepository.save(wedlockExtractShort);
    }

    @Override
    public void delete(WedlockExtractFull wedlockExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
