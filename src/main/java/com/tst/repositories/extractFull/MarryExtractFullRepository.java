package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.MarryExtractFull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarryExtractFullRepository extends JpaRepository<MarryExtractFull, Long> {

    List<MarryExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<MarryExtractFull> findByProjectNumberBookFileAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile);
}
