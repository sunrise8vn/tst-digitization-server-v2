package com.tst.services.wedlockExtractFull;

import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.extractFull.WedlockExtractFullDTO;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.WedlockExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WedlockExtractFullService implements IWedlockExtractFullService {

    private final WedlockExtractFullRepository wedlockExtractFullRepository;
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
    public void update(WedlockExtractFull wedlockExtractFull, WedlockExtractFullDTO wedlockExtractFullDTO) {
        genderTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerGender()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Giới tính của người xác nhận không tồn tại");
        });

        residenceTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerResidenceType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại cư trú của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người xác nhận không tồn tại");
        });

        intendedUseTypeRepository.findByCode(
                wedlockExtractFullDTO.getConfirmerIntendedUseType()
        ).orElseThrow(() -> {
           throw new DataNotFoundException("Loại mục đích sử dụng của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractFullDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                wedlockExtractFullDTO,
                wedlockExtractFull
        );

        wedlockExtractFull.setStatus(EInputStatus.IMPORTED);

        wedlockExtractFullRepository.save(wedlockExtractFull);
    }

    @Override
    public void delete(WedlockExtractFull wedlockExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
