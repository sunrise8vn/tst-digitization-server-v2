package com.tst.services.marryExtractShort;

import com.tst.models.dtos.extractShort.MarryExtractShortDTO;
import com.tst.models.entities.extractShort.MarryExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IMarryExtractShortService extends IGeneralService<MarryExtractShort, Long> {

    Optional<MarryExtractShort> findByIdAndStatus(Long id, EInputStatus status);

    Optional<MarryExtractShort> findByIdAndStatusBeforeCompare(Long id);

    void importBeforeCompare(
            MarryExtractShort marryExtractShort,
            MarryExtractShortDTO marryExtractShortDTO
    );

}
