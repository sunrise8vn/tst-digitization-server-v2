package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.DeathExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeathExtractFullRepository extends JpaRepository<DeathExtractFull, Long> {

    Optional<DeathExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<DeathExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

    List<DeathExtractFull> findByProjectAndImporterIsNull(Project project);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.project = :project " +
            "AND def.importer = :importer " +
            "AND def.status = 'NEW'"
    )
    List<DeathExtractFull> findByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.project = :project " +
            "AND def.importer = :importer " +
            "AND def.status = 'LATER_PROCESSING'"
    )
    List<DeathExtractFull> findByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.project = :project " +
            "AND def.importer = :importer " +
            "AND (def.status = 'IMPORTED' " +
            "OR def.status = 'MATCHING' " +
            "OR def.status = 'NOT_MATCHING')"
    )
    List<DeathExtractFull> findByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN DeathExtractShort AS des " +
            "ON def.projectNumberBookFile = des.projectNumberBookFile " +
            "WHERE def.accessPoint = :accessPoint " +
            "AND def.status = des.status " +
            "AND (def.status = 'NEW' " +
            "OR def.status = 'LATER_PROCESSING') " +
            "ORDER BY def.id DESC"
    )
    List<DeathExtractFull> findDeathSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
