package com.tst.services.marryExtractShort;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractShort.MarryExtractShortDTO;
import com.tst.models.entities.extractShort.MarryExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractShort.MarryExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MarryExtractShortService implements IMarryExtractShortService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final MarryExtractShortRepository marryExtractShortRepository;
    private final MaritalStatusTypeRepository maritalStatusTypeRepository;
    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<MarryExtractShort> findById(Long id) {
        return marryExtractShortRepository.findById(id);
    }

    @Override
    public Optional<MarryExtractShort> findByIdAndStatus(Long id, EInputStatus status) {
        return marryExtractShortRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<MarryExtractShort> findByIdForImporter(Long id) {
        return marryExtractShortRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<MarryExtractShort> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return marryExtractShortRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    @Transactional
    public void importBeforeCompare(MarryExtractShort marryExtractShort, MarryExtractShortDTO marryExtractShortDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                marryExtractShortDTO.getRegistrationType(),
                ERegistrationType.KH
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        maritalStatusTypeRepository.findByCode(
                marryExtractShortDTO.getMaritalStatus()
        ).orElseThrow(() -> {
            throw new DataInputException("Tình trạng hôn nhân không tồn tại");
        });

        residenceTypeRepository.findByCode(
                marryExtractShortDTO.getHusbandResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người chồng không tồn tại");
        });

        identificationTypeRepository.findByCode(
                marryExtractShortDTO.getHusbandIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người chồng không tồn tại");
        });

        residenceTypeRepository.findByCode(
                marryExtractShortDTO.getWifeResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người vợ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                marryExtractShortDTO.getWifeIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người vợ không tồn tại");
        });

        modelMapper.map(
                marryExtractShortDTO,
                marryExtractShort
        );

        if (marryExtractShort.getStatus() == EInputStatus.NOT_MATCHING
                || marryExtractShort.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractShortError(marryExtractShort.getAccessPoint(), marryExtractShort.getImporter());
            accessPointRepository.minusCountExtractShortError(marryExtractShort.getAccessPoint());
        }

        marryExtractShort.setStatus(EInputStatus.IMPORTED);

        marryExtractShortRepository.save(marryExtractShort);
    }

    @Override
    public void delete(MarryExtractShort marryExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
