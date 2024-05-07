package com.tst.services.wedlockExtractShort;

import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.extractShort.WedlockExtractShortDTO;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.repositories.IdentificationTypeRepository;
import com.tst.repositories.ResidenceTypeRepository;
import com.tst.repositories.extractShort.WedlockExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WedlockExtractShortService implements IWedlockExtractShortService {

    private final WedlockExtractShortRepository wedlockExtractShortRepository;

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
    public void update(WedlockExtractShort wedlockExtractShort, WedlockExtractShortDTO wedlockExtractShortDTO) {
        residenceTypeRepository.findByCode(
                wedlockExtractShortDTO.getConfirmerResidenceType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại cư trú của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractShortDTO.getConfirmerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người xác nhận không tồn tại");
        });

        identificationTypeRepository.findByCode(
                wedlockExtractShortDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                wedlockExtractShortDTO,
                wedlockExtractShort
        );

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
