package com.tst.services.parentsChildrenExtractShort;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.extractShort.ParentsChildrenExtractShortDTO;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.ConfirmationTypeRepository;
import com.tst.repositories.IdentificationTypeRepository;
import com.tst.repositories.RegistrationTypeDetailRepository;
import com.tst.repositories.ResidenceTypeRepository;
import com.tst.repositories.extractShort.ParentsChildrenExtractShortRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParentsChildrenExtractShortService implements IParentsChildrenExtractShortService {

    private final ParentsChildrenExtractShortRepository parentsChildrenExtractShortRepository;

    private final RegistrationTypeDetailRepository registrationTypeDetailRepository;
    private final ConfirmationTypeRepository confirmationTypeRepository;
    private final ResidenceTypeRepository residenceTypeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;

    private final ModelMapper modelMapper;


    @Override
    public Optional<ParentsChildrenExtractShort> findById(Long id) {
        return parentsChildrenExtractShortRepository.findById(id);
    }

    @Override
    public Optional<ParentsChildrenExtractShort> findByIdAndStatus(Long id, EInputStatus status) {
        return parentsChildrenExtractShortRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<ParentsChildrenExtractShort> findByIdForImporter(Long id) {
        return parentsChildrenExtractShortRepository.findByIdForImporter(id);
    }

    @Override
    public Optional<ParentsChildrenExtractShort> findNextIdForImporter(Long projectId, String userId, Long id, String tableName) {
        return parentsChildrenExtractShortRepository.findNextIdForImporter(projectId, userId, id, tableName);
    }

    @Override
    public void importBeforeCompare(ParentsChildrenExtractShort parentsChildrenExtractShort, ParentsChildrenExtractShortDTO parentsChildrenExtractShortDTO) {
        registrationTypeDetailRepository.findByCodeAndERegistrationType(
                parentsChildrenExtractShortDTO.getRegistrationType(),
                ERegistrationType.CMC
        ).orElseThrow(() -> {
           throw new DataInputException("Loại đăng ký không tồn tại");
        });

        confirmationTypeRepository.findByCode(
                parentsChildrenExtractShortDTO.getConfirmationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại xác nhận không tồn tại");
        });

        residenceTypeRepository.findByCode(
                parentsChildrenExtractShortDTO.getParentResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của cha mẹ không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractShortDTO.getParentIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của cha mẹ không tồn tại");
        });

        residenceTypeRepository.findByCode(
                parentsChildrenExtractShortDTO.getChildResidenceType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại cư trú của người con không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractShortDTO.getChildIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người con không tồn tại");
        });

        identificationTypeRepository.findByCode(
                parentsChildrenExtractShortDTO.getPetitionerIdentificationType()
        ).orElseThrow(() -> {
            throw new DataInputException("Loại giấy tờ tùy thân của người yêu cầu không tồn tại");
        });

        modelMapper.map(
                parentsChildrenExtractShortDTO,
                parentsChildrenExtractShort
        );

        parentsChildrenExtractShort.setStatus(EInputStatus.IMPORTED);

        parentsChildrenExtractShortRepository.save(parentsChildrenExtractShort);
    }

    @Override
    public void delete(ParentsChildrenExtractShort parentsChildrenExtractShort) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
