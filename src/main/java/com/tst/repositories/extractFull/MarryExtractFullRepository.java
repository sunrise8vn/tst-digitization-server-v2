package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.MarryExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarryExtractFullRepository extends JpaRepository<MarryExtractFull, Long> {

    List<MarryExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<MarryExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


    @Query("SELECT mef " +
            "FROM MarryExtractFull AS mef " +
            "JOIN MarryExtractShort AS mes " +
            "ON mef.projectNumberBookFile = mes.projectNumberBookFile " +
            "WHERE mef.accessPoint = :accessPoint " +
            "AND mef.status = mes.status " +
            "AND (mef.status = 'NEW' " +
            "OR mef.status = 'LATER_PROCESSING') " +
            "ORDER BY mef.id DESC"
    )
    List<MarryExtractFull> findMarrySameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
