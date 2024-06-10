package com.tst.services.wedlockExtractFull;

import com.tst.models.dtos.extractFull.WedlockExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IWedlockExtractFullService extends IGeneralService<WedlockExtractFull, Long> {

    Optional<WedlockExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<WedlockExtractFull> findByIdForImporter(Long id);

    Optional<WedlockExtractFull> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    Optional<WedlockExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id);

    Optional<WedlockExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id);

    void importBeforeCompare(
            WedlockExtractFull wedlockExtractFull,
            WedlockExtractFullDTO wedlockExtractFullDTO
    );

    void verifyComparedMatch(WedlockExtractFull wedlockExtractFull, User compareChecker);

    void verifyComparedNotMatch(WedlockExtractFull wedlockExtractFull, User compareChecker);

    void verifyCheckedComparedMatch(WedlockExtractFull wedlockExtractFull, User finalChecker);

    void verifyCheckedComparedNotMatch(WedlockExtractFull wedlockExtractFull, User finalChecker);

}
