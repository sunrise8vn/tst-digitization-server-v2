package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.MarryExtractShort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarryExtractShortRepository extends JpaRepository<MarryExtractShort, Long> {

    List<MarryExtractShort> findByProjectAndImporterIsNull(Project project);

    List<MarryExtractShort> findByAccessPointAndImporterIsNotNull(AccessPoint accessPoint);

}
