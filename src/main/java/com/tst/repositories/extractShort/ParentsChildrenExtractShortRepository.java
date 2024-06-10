package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParentsChildrenExtractShortRepository extends JpaRepository<ParentsChildrenExtractShort, Long> {

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    Long countAllByProjectAndAccessPointIsNull(Project project);

    ParentsChildrenExtractShort getByProjectNumberBookFile(ProjectNumberBookFile projectNumberBookFile);

    Optional<ParentsChildrenExtractShort> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pces.projectNumberBookFile = pnbf " +
            "WHERE pces.id = :id " +
            "AND (pces.status = 'NEW' " +
            "OR pces.status = 'IMPORTED' " +
            "OR pces.status = 'LATER_PROCESSING' " +
            "OR pces.status = 'NOT_MATCHING' " +
            "OR pces.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<ParentsChildrenExtractShort> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<ParentsChildrenExtractShort> findNextIdForImporter(long projectId, String userId, Long id, String tableName);

    List<ParentsChildrenExtractShort> findAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    List<ParentsChildrenExtractShort> findAllByProjectAndImporterIsNull(Project project);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pces.projectNumberBookFile = pnbf " +
            "WHERE pces.project = :project " +
            "AND pces.importer = :importer " +
            "AND pces.status <> 'ACCEPTED'"
    )
    List<ParentsChildrenExtractShort> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pces.projectNumberBookFile = pnbf " +
            "WHERE pces.project = :project " +
            "AND pces.importer = :importer " +
            "AND pces.status = 'NEW'"
    )
    List<ParentsChildrenExtractShort> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pces.projectNumberBookFile = pnbf " +
            "WHERE pces.project = :project " +
            "AND pces.importer = :importer " +
            "AND pces.status = 'LATER_PROCESSING'"
    )
    List<ParentsChildrenExtractShort> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pces.projectNumberBookFile = pnbf " +
            "WHERE pces.project = :project " +
            "AND pces.importer = :importer " +
            "AND (pces.status = 'IMPORTED' " +
            "OR pces.status = 'MATCHING' " +
            "OR pces.status = 'NOT_MATCHING')"
    )
    List<ParentsChildrenExtractShort> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ParentsChildrenExtractFull AS pcef " +
            "ON pces.projectNumberBookFile = pcef.projectNumberBookFile " +
            "WHERE pces.accessPoint = :accessPoint " +
            "AND pces.status = pcef.status " +
            "AND (pces.status = 'NEW' " +
            "OR pces.status = 'LATER_PROCESSING') " +
            "ORDER BY pces.id DESC"
    )
    List<ParentsChildrenExtractShort> findAllParentsChildrenSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
