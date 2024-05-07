package com.tst.services.parentsChildrenExtractFull;

import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.extractFull.ParentsChildrenExtractFullDTO;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.ConfirmationTypeRepository;
import com.tst.repositories.IdentificationTypeRepository;
import com.tst.repositories.RegistrationTypeDetailRepository;
import com.tst.repositories.ResidenceTypeRepository;
import com.tst.repositories.extractFull.ParentsChildrenExtractFullRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParentsChildrenExtractFullService implements IParentsChildrenExtractFullService {

    private final ParentsChildrenExtractFullRepository parentsChildrenExtractFullRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ConfirmationTypeRepository confirmationTypeRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;

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
    public void update(ParentsChildrenExtractFull parentsChildrenExtractFull, ParentsChildrenExtractFullDTO parentsChildrenExtractFullDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                parentsChildrenExtractFullDTO.getRegistrationType(),
                ERegistrationType.CMC
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại đăng ký không tồn tại");
        });

        confirmationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getConfirmationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại xác nhận không tồn tại");
        });

        residenceTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getParentResidenceType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại cư trú của cha mẹ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getParentIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của cha mẹ không tồn tại");
        });

        residenceTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getChildResidenceType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại cư trú của người con không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getChildIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người con không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractFullDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                parentsChildrenExtractFullDTO,
                parentsChildrenExtractFull
        );

        parentsChildrenExtractFull.setStatus(EInputStatus.IMPORTED);

        parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);
    }

    @Override
    public void delete(ParentsChildrenExtractFull parentsChildrenExtractFull) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
