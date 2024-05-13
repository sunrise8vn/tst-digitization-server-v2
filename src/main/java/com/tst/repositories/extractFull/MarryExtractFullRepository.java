package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.MarryExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarryExtractFullRepository extends JpaRepository<MarryExtractFull, Long> {

    Optional<MarryExtractFull> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT mef " +
            "FROM MarryExtractFull AS mef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mef.projectNumberBookFile = pnbf " +
            "WHERE mef.project = :project " +
            "AND mef.id = :id " +
            "AND (mef.status = 'NEW' " +
            "OR mef.status = 'IMPORTED'" +
            "OR mef.status = 'LATER_PROCESSING')"
    )
    Optional<MarryExtractFull> findByIdAndStatusBeforeCompare(Project project, Long id);


    Optional<MarryExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

    List<MarryExtractFull> findAllByProjectAndImporterIsNull(Project project);


    @Query("SELECT mef " +
            "FROM MarryExtractFull AS mef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mef.projectNumberBookFile = pnbf " +
            "WHERE mef.project = :project " +
            "AND mef.importer = :importer"
    )
    List<MarryExtractFull> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT mef " +
            "FROM MarryExtractFull AS mef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mef.projectNumberBookFile = pnbf " +
            "WHERE mef.project = :project " +
            "AND mef.importer = :importer " +
            "AND mef.status = 'NEW'"
    )
    List<MarryExtractFull> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT mef " +
            "FROM MarryExtractFull AS mef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mef.projectNumberBookFile = pnbf " +
            "WHERE mef.project = :project " +
            "AND mef.importer = :importer " +
            "AND mef.status = 'LATER_PROCESSING'"
    )
    List<MarryExtractFull> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT mef " +
            "FROM MarryExtractFull AS mef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mef.projectNumberBookFile = pnbf " +
            "WHERE mef.project = :project " +
            "AND mef.importer = :importer " +
            "AND (mef.status = 'IMPORTED' " +
            "OR mef.status = 'MATCHING' " +
            "OR mef.status = 'NOT_MATCHING')"
    )
    List<MarryExtractFull> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


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
    List<MarryExtractFull> findAllMarrySameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
