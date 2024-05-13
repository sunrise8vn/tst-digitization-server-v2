package com.tst.services.parentsChildrenExtractFull;

import com.tst.models.dtos.extractFull.ParentsChildrenExtractFullDTO;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IParentsChildrenExtractFullService extends IGeneralService<ParentsChildrenExtractFull, Long> {

    Optional<ParentsChildrenExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<ParentsChildrenExtractFull> findByIdAndStatusBeforeCompare(Long id);

    void importBeforeCompare(
            ParentsChildrenExtractFull parentsChildrenExtractFull,
            ParentsChildrenExtractFullDTO parentsChildrenExtractFullDTO
    );

}
