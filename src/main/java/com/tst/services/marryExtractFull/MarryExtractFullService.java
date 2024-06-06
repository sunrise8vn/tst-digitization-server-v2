package com.tst.services.marryExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.MarryExtractFullDTO;
import com.tst.models.entities.extractFull.MarryExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.MarryExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MarryExtractFullService implements IMarryExtractFullService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final MarryExtractFullRepository marryExtractFullRepository;
    private final MaritalStatusTypeRepository maritalStatusTypeRepository;
    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<MarryExtractFull> findById(Long id) {
        return marryExtractFullRepository.findById(id);
    }

    @Override
    public Optional<MarryExtractFull> findByIdAndStatus(Long id, EInputStatus status) {
        return marryExtractFullRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<MarryExtractFull> findByIdForImporter(Long id) {
        return marryExtractFullRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<MarryExtractFull> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return marryExtractFullRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    @Transactional
    public void importBeforeCompare(MarryExtractFull marryExtractFull, MarryExtractFullDTO marryExtractFullDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                marryExtractFullDTO.getRegistrationType(),
                ERegistrationType.KH
        ).orElseThrow(() -> {
            throw new DataInputException("Loại đăng ký không tồn tại");
        });

        maritalStatusTypeRepository.findByCode(
                marryExtractFullDTO.getMaritalStatus()
        ).orElseThrow(() -> {
            throw new DataInputException("Tình trạng hôn nhân không tồn tại");
        });

        residenceTypeRepository.findByCode(
                marryExtractFullDTO.getHusbandResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người chồng không tồn tại");
        });

        identificationTypeRepository.findByCode(
                marryExtractFullDTO.getHusbandIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người chồng không tồn tại");
        });

        residenceTypeRepository.findByCode(
                marryExtractFullDTO.getWifeResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người vợ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                marryExtractFullDTO.getWifeIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người vợ không tồn tại");
        });

        modelMapper.map(
                marryExtractFullDTO,
                marryExtractFull
        );

        if (marryExtractFull.getStatus() == EInputStatus.NOT_MATCHING
                || marryExtractFull.getStatus() == EInputStatus.CHECKED_NOT_MATCHING
        ) {
            accessPointHistoryRepository.minusCountExtractFullError(marryExtractFull.getAccessPoint(), marryExtractFull.getImporter());
            accessPointRepository.minusCountExtractFullError(marryExtractFull.getAccessPoint());
        }

        marryExtractFull.setStatus(EInputStatus.IMPORTED);

        marryExtractFullRepository.save(marryExtractFull);
    }

    @Override
    public void delete(MarryExtractFull marryExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
