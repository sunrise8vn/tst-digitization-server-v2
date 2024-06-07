package com.tst.services.deathExtractFull;

import com.tst.models.dtos.extractFull.DeathExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IDeathExtractFullService extends IGeneralService<DeathExtractFull, Long> {

    Optional<DeathExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<DeathExtractFull> findByIdForImporter(Long id);

    Optional<DeathExtractFull> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    Optional<DeathExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id);

    Optional<DeathExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id);

    void importBeforeCompare(
            DeathExtractFull deathExtractFull,
            DeathExtractFullDTO deathExtractFullDTO
    );

    void verifyCheckedMatch(DeathExtractFull deathExtractFull);

    void verifyCheckedNotMatch(DeathExtractFull deathExtractFull);

}
