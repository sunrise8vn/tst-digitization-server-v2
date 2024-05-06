package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WedlockExtractShortRepository extends JpaRepository<WedlockExtractShort, Long> {

    List<WedlockExtractShort> findByProjectAndImporterIsNull(Project project);

    List<WedlockExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

}
