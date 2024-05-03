package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.DeathExtractFull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeathExtractFullRepository extends JpaRepository<DeathExtractFull, Long> {

    List<DeathExtractFull> findByProjectAndImporterIsNull(Project project);
}
