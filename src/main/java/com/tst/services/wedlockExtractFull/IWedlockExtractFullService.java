package com.tst.services.wedlockExtractFull;

import com.tst.models.dtos.extractFull.WedlockExtractFullDTO;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IWedlockExtractFullService extends IGeneralService<WedlockExtractFull, Long> {

    Optional<WedlockExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<WedlockExtractFull> findByIdAndStatusBeforeCompare(Long id);

    void importBeforeCompare(
            WedlockExtractFull wedlockExtractFull,
            WedlockExtractFullDTO wedlockExtractFullDTO
    );

}
