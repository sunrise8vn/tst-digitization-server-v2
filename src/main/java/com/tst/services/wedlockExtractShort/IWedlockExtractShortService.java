package com.tst.services.wedlockExtractShort;

import com.tst.models.dtos.extractShort.WedlockExtractShortDTO;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IWedlockExtractShortService extends IGeneralService<WedlockExtractShort, Long> {

    Optional<WedlockExtractShort> findByIdAndStatus(Long id, EInputStatus status);

    Optional<WedlockExtractShort> findByIdAndStatusBeforeCompare(Long id);

    Optional<WedlockExtractShort> findNextIdAndStatusBeforeCompare(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    void importBeforeCompare(
            WedlockExtractShort wedlockExtractShort,
            WedlockExtractShortDTO wedlockExtractShortDTO
    );

}
