package com.tst.services.parentsChildrenExtractShort;

import com.tst.models.dtos.extractShort.ParentsChildrenExtractShortDTO;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IParentsChildrenExtractShortService extends IGeneralService<ParentsChildrenExtractShort, Long> {

    Optional<ParentsChildrenExtractShort> findByIdAndStatus(Long id, EInputStatus status);

    Optional<ParentsChildrenExtractShort> findByIdAndStatusBeforeCompare(Long id);

    void importBeforeCompare(
            ParentsChildrenExtractShort parentsChildrenExtractShort,
            ParentsChildrenExtractShortDTO parentsChildrenExtractShortDTO
    );

}
