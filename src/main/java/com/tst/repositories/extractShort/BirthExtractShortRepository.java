package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BirthExtractShortRepository extends JpaRepository<BirthExtractShort, Long> {

    List<BirthExtractShort> findByProjectAndImporterIsNull(Project project);

    List<BirthExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

}
