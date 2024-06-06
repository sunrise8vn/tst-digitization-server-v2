package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WedlockExtractFullRepository extends JpaRepository<WedlockExtractFull, Long> {

    Optional<WedlockExtractFull> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wef.projectNumberBookFile = pnbf " +
            "WHERE wef.id = :id " +
            "AND (wef.status = 'NEW' " +
            "OR wef.status = 'IMPORTED' " +
            "OR wef.status = 'LATER_PROCESSING' " +
            "OR wef.status = 'NOT_MATCHING' " +
            "OR wef.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<WedlockExtractFull> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<WedlockExtractFull> findNextIdForImporter(long projectId, String userId, Long id, String tableName);


    Optional<WedlockExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    List<WedlockExtractFull> findAllByProjectAndImporterIsNull(Project project);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wef.projectNumberBookFile = pnbf " +
            "WHERE wef.project = :project " +
            "AND wef.importer = :importer " +
            "AND wef.status <> 'ACCEPTED'"
    )
    List<WedlockExtractFull> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wef.projectNumberBookFile = pnbf " +
            "WHERE wef.project = :project " +
            "AND wef.importer = :importer " +
            "AND wef.status = 'NEW'"
    )
    List<WedlockExtractFull> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wef.projectNumberBookFile = pnbf " +
            "WHERE wef.project = :project " +
            "AND wef.importer = :importer " +
            "AND wef.status = 'LATER_PROCESSING'"
    )
    List<WedlockExtractFull> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON wef.projectNumberBookFile = pnbf " +
            "WHERE wef.project = :project " +
            "AND wef.importer = :importer " +
            "AND (wef.status = 'IMPORTED' " +
            "OR wef.status = 'MATCHING' " +
            "OR wef.status = 'NOT_MATCHING')"
    )
    List<WedlockExtractFull> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN WedlockExtractShort AS wes " +
            "ON wef.projectNumberBookFile = wes.projectNumberBookFile " +
            "WHERE wef.accessPoint = :accessPoint " +
            "AND wef.status = wes.status " +
            "AND (wef.status = 'NEW' " +
            "OR wef.status = 'LATER_PROCESSING') " +
            "ORDER BY wef.id DESC"
    )
    List<WedlockExtractFull> findAllWedlockSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
