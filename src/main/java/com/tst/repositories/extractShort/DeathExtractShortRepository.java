package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeathExtractShortRepository extends JpaRepository<DeathExtractShort, Long> {

    List<DeathExtractShort> findByProjectAndImporterIsNull(Project project);

    List<DeathExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT des " +
            "FROM DeathExtractShort AS des " +
            "JOIN DeathExtractFull AS def " +
            "ON des.projectNumberBookFile = def.projectNumberBookFile " +
            "WHERE des.accessPoint = :accessPoint " +
            "AND des.status = def.status " +
            "AND (des.status = 'NEW' " +
            "OR des.status = 'LATER_PROCESSING') " +
            "ORDER BY des.id DESC"
    )
    List<DeathExtractShort> findDeathSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
