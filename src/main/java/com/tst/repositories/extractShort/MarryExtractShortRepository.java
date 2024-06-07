package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractShort.MarryExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarryExtractShortRepository extends JpaRepository<MarryExtractShort, Long> {

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    MarryExtractShort getByProjectNumberBookFile(ProjectNumberBookFile projectNumberBookFile);

    Optional<MarryExtractShort> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.id = :id " +
            "AND (mes.status = 'NEW' " +
            "OR mes.status = 'IMPORTED' " +
            "OR mes.status = 'LATER_PROCESSING' " +
            "OR mes.status = 'NOT_MATCHING' " +
            "OR mes.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<MarryExtractShort> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<MarryExtractShort> findNextIdForImporter(long projectId, String userId, Long id, String tableName);

    List<MarryExtractShort> findAllByProjectAndImporterIsNull(Project project);

    List<MarryExtractShort> findAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT mes " +
            "FROM MarryExtractShort AS mes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON mes.projectNumberBookFile = pnbf " +
            "WHERE mes.project = :project " +
            "AND mes.importer = :importer " +
            "AND mes.status <> 'ACCEPTED'"
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
