package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.BirthExtractFull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BirthExtractFullRepository extends JpaRepository<BirthExtractFull, Long> {

    List<BirthExtractFull> findByProjectAndImporterIsNull(Project project);
}
