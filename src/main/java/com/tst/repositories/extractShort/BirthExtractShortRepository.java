package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.User;
import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BirthExtractShortRepository extends JpaRepository<BirthExtractShort, Long> {

    Long countAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);

    Long countAllByAccessPointAndStatus(AccessPoint accessPoint, EInputStatus status);

    BirthExtractShort getByProjectNumberBookFile(ProjectNumberBookFile projectNumberBookFile);

    Optional<BirthExtractShort> findByIdAndStatus(Long id, EInputStatus status);


    @Query("SELECT bes " +
            "FROM BirthExtractShort AS bes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bes.projectNumberBookFile = pnbf " +
            "WHERE bes.id = :id " +
            "AND (bes.status = 'NEW' " +
            "OR bes.status = 'IMPORTED' " +
            "OR bes.status = 'LATER_PROCESSING' " +
            "OR bes.status = 'NOT_MATCHING' " +
            "OR bes.status = 'CHECKED_NOT_MATCHING'" +
            ")"
    )
    Optional<BirthExtractShort> findByIdForImporter(Long id);


    @Query(value = "CALL sp_find_next_item_all_table_by_id(:projectId, :userId, :id, :tableName)", nativeQuery = true)
    Optional<BirthExtractShort> findNextIdForImporter(long projectId, String userId, Long id, String tableName);

    List<BirthExtractShort> findAllByProjectAndImporterIsNull(Project project);

    List<BirthExtractShort> findAllByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT bes " +
            "FROM BirthExtractShort AS bes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bes.projectNumberBookFile = pnbf " +
            "WHERE bes.project = :project " +
            "AND bes.importer = :importer " +
            "AND bes.status <> 'ACCEPTED'"
    )
    List<BirthExtractShort> findAllByProjectAndImporter(Project project, User importer);


    @Query("SELECT bes " +
            "FROM BirthExtractShort AS bes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bes.projectNumberBookFile = pnbf " +
            "WHERE bes.project = :project " +
            "AND bes.importer = :importer " +
            "AND bes.status = 'NEW'"
    )
    List<BirthExtractShort> findAllByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT bes " +
            "FROM BirthExtractShort AS bes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bes.projectNumberBookFile = pnbf " +
            "WHERE bes.project = :project " +
            "AND bes.importer = :importer " +
            "AND bes.status = 'LATER_PROCESSING'"
    )
    List<BirthExtractShort> findAllByProjectAndImporterAndStatusLater(Project project, User importer);


    @Query("SELECT bes " +
            "FROM BirthExtractShort AS bes " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bes.projectNumberBookFile = pnbf " +
            "WHERE bes.project = :project " +
            "AND bes.importer = :importer " +
            "AND (bes.status = 'IMPORTED' " +
            "OR bes.status = 'MATCHING' " +
            "OR bes.status = 'NOT_MATCHING')"
    )
    List<BirthExtractShort> findAllByProjectAndImporterAndStatusImported(Project project, User importer);


    @Query("SELECT bes " +
            "FROM BirthExtractShort AS bes " +
            "JOIN BirthExtractFull AS bef " +
            "ON bes.projectNumberBookFile = bef.projectNumberBookFile " +
            "WHERE bes.accessPoint = :accessPoint " +
            "AND bes.status = bef.status " +
            "AND (bes.status = 'NEW' " +
            "OR bes.status = 'LATER_PROCESSING') " +
            "ORDER BY bes.id DESC"
    )
    List<BirthExtractShort> findAllBirthSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
