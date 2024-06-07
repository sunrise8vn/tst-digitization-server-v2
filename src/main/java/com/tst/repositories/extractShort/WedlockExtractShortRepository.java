package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WedlockExtractShortRepository extends JpaRepository<WedlockExtractShort, Long> {

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    WedlockExtractShort getByProjectNumberBookFile(ProjectNumberBookFile projectNumberBookFile);

    Optional<WedlockExtractShort> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wes.projectNumberBookFile = pnbf " +
            "WHERE wes.id = :id " +
            "AND (wes.status = 'NEW' " +
            "OR wes.status = 'IMPORTED' " +
            "OR wes.status = 'LATER_PROCESSING' " +
            "OR wes.status = 'NOT_MATCHING' " +
            "OR wes.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<WedlockExtractShort> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<WedlockExtractShort> findNextIdForImporter(long projectId, String userId, Long id, String tableName);

    List<WedlockExtractShort> findAllByProjectAndImporterIsNull(Project project);

    List<WedlockExtractShort> findAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wes.projectNumberBookFile = pnbf " +
            "WHERE wes.project = :project " +
            "AND wes.importer = :importer " +
            "AND wes.status <> 'ACCEPTED'"
    )
    List<WedlockExtractShort> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wes.projectNumberBookFile = pnbf " +
            "WHERE wes.project = :project " +
            "AND wes.importer = :importer " +
            "AND wes.status = 'NEW'"
    )
    List<WedlockExtractShort> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wes.projectNumberBookFile = pnbf " +
            "WHERE wes.project = :project " +
            "AND wes.importer = :importer " +
            "AND wes.status = 'LATER_PROCESSING'"
    )
    List<WedlockExtractShort> findAllByProjectAndImporterAndStatusLater(Project project, User importer);
    

    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wes.projectNumberBookFile = pnbf " +
            "WHERE wes.project = :project " +
            "AND wes.importer = :importer " +
            "AND (wes.status = 'IMPORTED' " +
            "OR wes.status = 'MATCHING' " +
            "OR wes.status = 'NOT_MATCHING')"
    )
    List<WedlockExtractShort> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN WedlockExtractFull AS wef " +
            "ON wes.projectNumberBookFile = wef.projectNumberBookFile " +
            "WHERE wes.accessPoint = :accessPoint " +
            "AND wes.status = wef.status " +
            "AND (wes.status = 'NEW' " +
            "OR wes.status = 'LATER_PROCESSING') " +
            "ORDER BY wes.id DESC"
    )
    List<WedlockExtractShort> findAllWedlockSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
