package com.tst.services.birthExtractShort;

import com.tst.models.dtos.extractShort.BirthExtractShortDTO;
import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IBirthExtractShortService extends IGeneralService<BirthExtractShort, Long> {

    Optional<BirthExtractShort> findByIdAndStatus(Long id, EInputStatus status);

    Optional<BirthExtractShort> findByIdForImporter(Long id);

    Optional<BirthExtractShort> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    void importBeforeCompare(
            BirthExtractShort birthExtractShort,
            BirthExtractShortDTO birthExtractShortDTO
    );

}
