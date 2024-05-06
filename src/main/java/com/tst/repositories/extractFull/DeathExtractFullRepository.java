package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeathExtractFullRepository extends JpaRepository<DeathExtractFull, Long> {

    List<DeathExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<DeathExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

}
