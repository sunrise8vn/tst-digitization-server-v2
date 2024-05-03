package com.tst.repositories.extractShort;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.DeathExtractShort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeathExtractShortRepository extends JpaRepository<DeathExtractShort, Long> {

    List<DeathExtractShort> findByProjectAndImporterIsNull(Project project);
}
