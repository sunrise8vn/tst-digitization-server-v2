package com.tst.services.marryExtractFull;

import com.tst.models.dtos.extractFull.MarryExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
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

    Optional<MarryExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id);

    Optional<MarryExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id);

    void importBeforeCompare(
            MarryExtractFull marryExtractFull,
            MarryExtractFullDTO marryExtractFullDTO
    );

    void verifyComparedMatch(MarryExtractFull marryExtractFull, User compareChecker);

    void verifyComparedNotMatch(MarryExtractFull marryExtractFull, User compareChecker);

    void verifyCheckedComparedMatch(MarryExtractFull marryExtractFull, User finalChecker);

    void verifyCheckedComparedNotMatch(MarryExtractFull marryExtractFull, User finalChecker);

}
