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

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    Long countAllByProjectAndAccessPointIsNull(Project project);

    Long countAllByProjectAndStatus(Project project, EInputStatus status);

    Optional<DeathExtractFull> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.id = :id " +
            "AND (def.status = 'NEW' " +
            "OR def.status = 'IMPORTED' " +
            "OR def.status = 'LATER_PROCESSING' " +
            "OR def.status = 'NOT_MATCHING' " +
            "OR def.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<DeathExtractFull> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<DeathExtractFull> findNextIdForImporter(long projectId, String userId, Long id, String tableName);


    Optional<DeathExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "WHERE def.id > :id " +
            "AND def.status = :status " +
            "AND def.project = :project"
    )
    Optional<DeathExtractFull> findNextIdByStatusForChecked(Project project, EInputStatus status, Long id);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "WHERE def.id < :id " +
            "AND def.status = :status " +
            "AND def.project = :project"
    )
    Optional<DeathExtractFull> findPrevIdByStatusForChecked(Project project, EInputStatus status, Long id);


    List<DeathExtractFull> findAllByProjectAndImporterIsNull(Project project);

    List<DeathExtractFull> findAllByProjectAndAccessPointIsNull(Project project);

    List<DeathExtractFull> findAllByProjectAndStatus(Project project, EInputStatus status);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.project = :project " +
            "AND def.importer = :importer " +
            "AND def.status <> 'RELEASED'"
    )
    List<DeathExtractFull> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.project = :project " +
            "AND def.importer = :importer " +
            "AND def.status = 'NEW'"
    )
    List<DeathExtractFull> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT def " +
            "FROM DeathExtractFull AS def " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON def.projectNumberBookFile = pnbf " +
            "WHERE def.project = :project " +
            "AND def.importer = :importer " +
            "AND def.status = 'LATER_PROCESSING'"
    )
    List<DeathExtractFull> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


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
    List<DeathExtractFull> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


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
    List<DeathExtractFull> findAllDeathSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
