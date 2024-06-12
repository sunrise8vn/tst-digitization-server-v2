package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BirthExtractFullRepository extends JpaRepository<BirthExtractFull, Long> {

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    Long countAllByProjectAndAccessPointIsNull(Project project);

    Optional<BirthExtractFull> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.id = :id " +
            "AND (bef.status = 'NEW' " +
            "OR bef.status = 'IMPORTED' " +
            "OR bef.status = 'LATER_PROCESSING' " +
            "OR bef.status = 'NOT_MATCHING' " +
            "OR bef.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<BirthExtractFull> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<BirthExtractFull> findNextIdForImporter(long projectId, String userId, Long id, String tableName);


    Optional<BirthExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "WHERE bef.id > :id " +
            "AND bef.status = :status " +
            "AND bef.project = :project"
    )
    Optional<BirthExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "WHERE bef.id < :id " +
            "AND bef.status = :status " +
            "AND bef.project = :project"
    )
    Optional<BirthExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id);


    List<BirthExtractFull> findAllByProjectAndImporterIsNull(Project project);

    List<BirthExtractFull> findAllByProjectAndAccessPointIsNull(Project project);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.project = :project " +
            "AND bef.importer = :importer " +
            "AND bef.status <> 'ACCEPTED'"
    )
    List<BirthExtractFull> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.project = :project " +
            "AND bef.importer = :importer " +
            "AND bef.status = 'NEW'"
    )
    List<BirthExtractFull> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.project = :project " +
            "AND bef.importer = :importer " +
            "AND bef.status = 'LATER_PROCESSING'"
    )
    List<BirthExtractFull> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.project = :project " +
            "AND bef.importer = :importer " +
            "AND (bef.status = 'IMPORTED' " +
            "OR bef.status = 'MATCHING' " +
            "OR bef.status = 'NOT_MATCHING')"
    )
    List<BirthExtractFull> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN BirthExtractShort AS bes " +
            "ON bef.projectNumberBookFile = bes.projectNumberBookFile " +
            "WHERE bef.accessPoint = :accessPoint " +
            "AND bef.status = bes.status " +
            "AND (bef.status = 'NEW' " +
            "OR bef.status = 'LATER_PROCESSING') " +
            "ORDER BY bef.id DESC"
    )
    List<BirthExtractFull> findAllBirthSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "WHERE bef.project = :project " +
            "AND bef.status = :status"
    )
    List<BirthExtractFull> findAllByProjectAndStatus(Project project, EInputStatus status);

}
