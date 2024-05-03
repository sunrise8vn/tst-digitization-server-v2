package com.tst.repositories.extractShort;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.BirthExtractShort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BirthExtractShortRepository extends JpaRepository<BirthExtractShort, Long> {

    List<BirthExtractShort> findByProjectAndImporterIsNull(Project project);
}
