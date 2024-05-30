package com.tst.services.birthExtractFull;

import com.tst.models.dtos.extractFull.BirthExtractFullDTO;
import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IBirthExtractFullService extends IGeneralService<BirthExtractFull, Long> {

    Optional<BirthExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<BirthExtractFull> findByIdForImporter(Long id);

    Optional<BirthExtractFull> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    void importBeforeCompare(
            BirthExtractFull birthExtractFull,
            BirthExtractFullDTO birthExtractFullDTO
    );

}
