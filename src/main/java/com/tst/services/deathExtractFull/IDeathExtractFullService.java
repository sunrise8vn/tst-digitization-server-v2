package com.tst.services.deathExtractFull;

import com.tst.models.dtos.extractFull.DeathExtractFullDTO;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IDeathExtractFullService extends IGeneralService<DeathExtractFull, Long> {

    Optional<DeathExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<DeathExtractFull> findByIdAndStatusBeforeCompare(Long id);

    void importBeforeCompare(
            DeathExtractFull deathExtractFull,
            DeathExtractFullDTO deathExtractFullDTO
    );

}
