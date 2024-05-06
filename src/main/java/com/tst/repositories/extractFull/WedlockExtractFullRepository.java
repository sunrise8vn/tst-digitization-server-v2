package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WedlockExtractFullRepository extends JpaRepository<WedlockExtractFull, Long> {

    List<WedlockExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<WedlockExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

}
