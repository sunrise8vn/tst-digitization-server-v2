package com.tst.services.marryExtractFull;

import com.tst.models.dtos.extractFull.MarryExtractFullDTO;
import com.tst.models.entities.extractFull.MarryExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IMarryExtractFullService extends IGeneralService<MarryExtractFull, Long> {

    Optional<MarryExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<MarryExtractFull> findByIdForImporter(Long id);

    Optional<MarryExtractFull> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    void importBeforeCompare(
            MarryExtractFull marryExtractFull,
            MarryExtractFullDTO marryExtractFullDTO
    );

}
