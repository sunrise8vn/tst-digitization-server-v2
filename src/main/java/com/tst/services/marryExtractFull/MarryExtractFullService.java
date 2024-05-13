package com.tst.services.marryExtractFull;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractFull.MarryExtractFullDTO;
import com.tst.models.entities.extractFull.MarryExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.IdentificationTypeRepository;
import com.tst.repositories.MaritalStatusTypeRepository;
import com.tst.repositories.RegistrationTypeDetailRepository;
import com.tst.repositories.ResidenceTypeRepository;
import com.tst.repositories.extractFull.MarryExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MarryExtractFullService implements IMarryExtractFullService {

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
    public Optional<MarryExtractFull> findByIdAndStatusBeforeCompare(Long id) {
        return marryExtractFullRepository.findByIdAndStatusBeforeCompare(id);
    }

    @Override
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
