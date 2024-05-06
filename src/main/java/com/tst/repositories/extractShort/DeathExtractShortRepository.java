package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeathExtractShortRepository extends JpaRepository<DeathExtractShort, Long> {

    List<DeathExtractShort> findByProjectAndImporterIsNull(Project project);

    List<DeathExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

}
