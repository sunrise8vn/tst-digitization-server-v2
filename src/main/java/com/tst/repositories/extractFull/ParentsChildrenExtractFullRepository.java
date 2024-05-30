package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParentsChildrenExtractFullRepository extends JpaRepository<ParentsChildrenExtractFull, Long> {

    Optional<ParentsChildrenExtractFull> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT pcef " +
            "FROM ParentsChildrenExtractFull AS pcef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pcef.projectNumberBookFile = pnbf " +
            "WHERE pcef.id = :id " +
            "AND (pcef.status = 'NEW' " +
            "OR pcef.status = 'IMPORTED' " +
            "OR pcef.status = 'LATER_PROCESSING' " +
            "OR pcef.status = 'NOT_MATCHING' " +
            "OR pcef.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<ParentsChildrenExtractFull> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<ParentsChildrenExtractFull> findNextIdForImporter(long projectId, String userId, Long id, String tableName);


    Optional<ParentsChildrenExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

    List<ParentsChildrenExtractFull> findByProjectAndImporterIsNull(Project project);


    @Query("SELECT pcef " +
            "FROM ParentsChildrenExtractFull AS pcef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pcef.projectNumberBookFile = pnbf " +
            "WHERE pcef.project = :project " +
            "AND pcef.importer = :importer " +
            "AND pcef.status <> 'ACCEPTED'"
    )
    List<ParentsChildrenExtractFull> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT pcef " +
            "FROM ParentsChildrenExtractFull AS pcef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pcef.projectNumberBookFile = pnbf " +
            "WHERE pcef.project = :project " +
            "AND pcef.importer = :importer " +
            "AND (pcef.status = 'NEW' " +
            "OR pcef.status = 'LATER_PROCESSING')"
    )
    List<ParentsChildrenExtractFull> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT pcef " +
            "FROM ParentsChildrenExtractFull AS pcef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pcef.projectNumberBookFile = pnbf " +
            "WHERE pcef.project = :project " +
            "AND pcef.importer = :importer " +
            "AND pcef.status = 'LATER_PROCESSING'"
    )
    List<ParentsChildrenExtractFull> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT pcef " +
            "FROM ParentsChildrenExtractFull AS pcef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON pcef.projectNumberBookFile = pnbf " +
            "WHERE pcef.project = :project " +
            "AND pcef.importer = :importer " +
            "AND (pcef.status = 'IMPORTED' " +
            "OR pcef.status = 'MATCHING' " +
            "OR pcef.status = 'NOT_MATCHING')"
    )
    List<ParentsChildrenExtractFull> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT pcef " +
            "FROM ParentsChildrenExtractFull AS pcef " +
            "JOIN ParentsChildrenExtractShort AS pces " +
            "ON pcef.projectNumberBookFile = pces.projectNumberBookFile " +
            "WHERE pcef.accessPoint = :accessPoint " +
            "AND pcef.status = pces.status " +
            "AND (pcef.status = 'NEW' " +
            "OR pcef.status = 'LATER_PROCESSING') " +
            "ORDER BY pcef.id DESC"
    )
    List<ParentsChildrenExtractFull> findAllParentsChildrenSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
