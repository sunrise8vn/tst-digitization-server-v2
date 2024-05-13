package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractShort.MarryExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarryExtractShortRepository extends JpaRepository<MarryExtractShort, Long> {

    Optional<MarryExtractShort> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.id = :id " +
            "AND (mes.status = 'NEW' " +
            "OR mes.status = 'IMPORTED'" +
            "OR mes.status = 'LATER_PROCESSING')"
    )
    Optional<MarryExtractShort> findByIdAndStatusBeforeCompare(Long id);


    List<MarryExtractShort> findAllByProjectAndImporterIsNull(Project project);

    List<MarryExtractShort> findAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.project = :project " +
            "AND mes.importer = :importer"
    )
    List<MarryExtractShort> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.project = :project " +
            "AND mes.importer = :importer " +
            "AND mes.status = 'NEW'"
    )
    List<MarryExtractShort> findByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.project = :project " +
            "AND mes.importer = :importer " +
            "AND mes.status = 'LATER_PROCESSING'"
    )
    List<MarryExtractShort> findByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.project = :project " +
            "AND mes.importer = :importer " +
            "AND (mes.status = 'IMPORTED' " +
            "OR mes.status = 'MATCHING' " +
            "OR mes.status = 'NOT_MATCHING')"
    )
    List<MarryExtractShort> findByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN MarryExtractFull AS mef " +
            "ON mes.projectNumberBookFile = mef.projectNumberBookFile " +
            "WHERE mes.accessPoint = :accessPoint " +
            "AND mes.status = mef.status " +
            "AND (mes.status = 'NEW' " +
            "OR mes.status = 'LATER_PROCESSING') " +
            "ORDER BY mes.id DESC"
    )
    List<MarryExtractShort> findMarrySameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
