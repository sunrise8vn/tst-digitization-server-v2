package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.MarryExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarryExtractShortRepository extends JpaRepository<MarryExtractShort, Long> {

    List<MarryExtractShort> findByProjectAndImporterIsNull(Project project);

    List<MarryExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN MarryExtractFull AS mef " +
            "ON mes.projectNumberBookFile = mef.projectNumberBookFile " +
            "WHERE mes.accessPoint = :accessPoint " +
            "AND mes.status = mef.status " +
            "AND (mes.status = 'NEW' " +
            "OR mes.status = 'LATER_PROCESSING')"
    )
    List<MarryExtractShort> findMarrySameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
