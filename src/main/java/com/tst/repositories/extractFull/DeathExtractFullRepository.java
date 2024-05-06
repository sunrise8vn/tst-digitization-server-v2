package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeathExtractFullRepository extends JpaRepository<DeathExtractFull, Long> {

    List<DeathExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<DeathExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN DeathExtractShort AS des " +
            "ON def.projectNumberBookFile = des.projectNumberBookFile " +
            "WHERE def.accessPoint = :accessPoint " +
            "AND def.status = des.status " +
            "AND (def.status = 'NEW' " +
            "OR def.status = 'LATER_PROCESSING')"
    )
    List<DeathExtractFull> findDeathSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
