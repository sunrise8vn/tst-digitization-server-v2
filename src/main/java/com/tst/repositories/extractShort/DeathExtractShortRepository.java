package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractShort.DeathExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeathExtractShortRepository extends JpaRepository<DeathExtractShort, Long> {

    Optional<DeathExtractShort> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT des " +
            "FROM DeathExtractShort AS des " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON des.projectNumberBookFile = pnbf " +
            "WHERE des.id = :id " +
            "AND (des.status = 'NEW' " +
            "OR des.status = 'IMPORTED' " +
            "OR des.status = 'LATER_PROCESSING' " +
            "OR des.status = 'NOT_MATCHING' " +
            "OR des.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<DeathExtractShort> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<DeathExtractShort> findNextIdForImporter(long projectId, String userId, Long id, String tableName);

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    List<DeathExtractShort> findAllByProjectAndImporterIsNull(Project project);

    List<DeathExtractShort> findAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT des " +
            "FROM DeathExtractShort AS des " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON des.projectNumberBookFile = pnbf " +
            "WHERE des.project = :project " +
            "AND des.importer = :importer " +
            "AND des.status <> 'ACCEPTED'"
    )
    List<DeathExtractShort> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT des " +
            "FROM DeathExtractShort AS des " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON des.projectNumberBookFile = pnbf " +
            "WHERE des.project = :project " +
            "AND des.importer = :importer " +
            "AND des.status = 'NEW'"
    )
    List<DeathExtractShort> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT des " +
            "FROM DeathExtractShort AS des " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON des.projectNumberBookFile = pnbf " +
            "WHERE des.project = :project " +
            "AND des.importer = :importer " +
            "AND des.status = 'LATER_PROCESSING'"
    )
    List<DeathExtractShort> findAllByProjectAndImporterAndStatusLater(Project project, User importer);
    

    @Query("SELECT des " +
            "FROM DeathExtractShort AS des " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON des.projectNumberBookFile = pnbf " +
            "WHERE des.project = :project " +
            "AND des.importer = :importer " +
            "AND (des.status = 'IMPORTED' " +
            "OR des.status = 'MATCHING' " +
            "OR des.status = 'NOT_MATCHING')"
    )
    List<DeathExtractShort> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


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
    List<DeathExtractShort> findAllDeathSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
