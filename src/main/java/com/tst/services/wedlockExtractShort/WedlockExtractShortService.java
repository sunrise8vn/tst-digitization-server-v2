package com.tst.services.wedlockExtractShort;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractShort.WedlockExtractShortDTO;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.repositories.*;
import com.tst.repositories.extractShort.WedlockExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WedlockExtractShortService implements IWedlockExtractShortService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final WedlockExtractShortRepository wedlockExtractShortRepository;
    private final GenderTypeRepository genderTypeRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<WedlockExtractShort> findById(Long id) {
        return wedlockExtractShortRepository.findById(id);
    }

    @Override
    public Optional<WedlockExtractShort> findByIdAndStatus(Long id, EInputStatus status) {
        return wedlockExtractShortRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<WedlockExtractShort> findByIdForImporter(Long id) {
        return wedlockExtractShortRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<WedlockExtractShort> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return wedlockExtractShortRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    @Transactional
    public void importBeforeCompare(WedlockExtractShort wedlockExtractShort, WedlockExtractShortDTO wedlockExtractShortDTO) {
        genderTypeRepository.findByCode(
                wedlockExtractShortDTO.getConfirmerGender()
        ).orElseThrow(() -> {
            throw new DataInputException("Giới tính của người xác nhận không tồn tại");
        });

        residenceTypeRepository.findByCode(
                wedlockExtractShortDTO.getConfirmerResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractShortDTO.getConfirmerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractShortDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                wedlockExtractShortDTO,
                wedlockExtractShort
        );

        if (wedlockExtractShort.getStatus() == EInputStatus.NOT_MATCHING
                || wedlockExtractShort.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractShortError(wedlockExtractShort.getAccessPoint(), wedlockExtractShort.getImporter());
            accessPointRepository.minusCountExtractShortError(wedlockExtractShort.getAccessPoint());
        }

        wedlockExtractShort.setStatus(EInputStatus.IMPORTED);

        wedlockExtractShortRepository.save(wedlockExtractShort);
    }

    @Override
    public void delete(WedlockExtractShort wedlockExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
