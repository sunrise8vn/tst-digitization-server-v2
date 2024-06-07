package com.tst.services.parentsChildrenExtractFull;

import com.tst.models.dtos.extractFull.ParentsChildrenExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IParentsChildrenExtractFullService extends IGeneralService<ParentsChildrenExtractFull, Long> {

    Optional<ParentsChildrenExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<ParentsChildrenExtractFull> findByIdForImporter(Long id);

    Optional<ParentsChildrenExtractFull> findNextIdForImporter(
            Long projectId,
            String userId,
            Long id,
            String tableName
    );

    Optional<ParentsChildrenExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id);

    Optional<ParentsChildrenExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id);

    void importBeforeCompare(
            ParentsChildrenExtractFull parentsChildrenExtractFull,
            ParentsChildrenExtractFullDTO parentsChildrenExtractFullDTO
    );

    void verifyCheckedMatch(ParentsChildrenExtractFull parentsChildrenExtractFull);

    void verifyCheckedNotMatch(ParentsChildrenExtractFull parentsChildrenExtractFull);

}
