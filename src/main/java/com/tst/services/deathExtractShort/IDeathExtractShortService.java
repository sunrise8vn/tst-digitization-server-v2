package com.tst.services.deathExtractShort;

import com.tst.models.dtos.extractShort.DeathExtractShortDTO;
import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IDeathExtractShortService extends IGeneralService<DeathExtractShort, Long> {

    Optional<DeathExtractShort> findByIdAndStatus(Long id, EInputStatus status);

    Optional<DeathExtractShort> findByIdForImporter(Long id);

    Optional<DeathExtractShort> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    void importBeforeCompare(
            DeathExtractShort deathExtractShort,
            DeathExtractShortDTO deathExtractShortDTO
    );

}
