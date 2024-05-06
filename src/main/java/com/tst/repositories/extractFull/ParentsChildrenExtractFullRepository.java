package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParentsChildrenExtractFullRepository extends JpaRepository<ParentsChildrenExtractFull, Long> {

    List<ParentsChildrenExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<ParentsChildrenExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

}
