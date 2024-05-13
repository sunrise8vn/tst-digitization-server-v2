package com.tst.services.birthExtractFull;

import com.tst.models.dtos.extractFull.BirthExtractFullDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.models.enums.EInputStatus;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IBirthExtractFullService extends IGeneralService<BirthExtractFull, Long> {

    Optional<BirthExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<BirthExtractFull> findByIdAndStatusBeforeCompare(Project project, Long id);

    void update(
            BirthExtractFull birthExtractFull,
            BirthExtractFullDTO birthExtractFullDTO
    );

}
