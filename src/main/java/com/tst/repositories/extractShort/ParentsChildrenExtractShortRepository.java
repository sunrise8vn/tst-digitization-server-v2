package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentsChildrenExtractShortRepository extends JpaRepository<ParentsChildrenExtractShort, Long> {

    List<ParentsChildrenExtractShort> findByProjectAndImporterIsNull(Project project);

    List<ParentsChildrenExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

}
