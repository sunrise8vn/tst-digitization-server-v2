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

    Optional<BirthExtractFull> findByIdAndStatus(Long id, EInputStatus status);

    Optional<BirthExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);

    List<BirthExtractFull> findByProjectAndImporterIsNull(Project project);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.project = :project " +
            "AND bef.importer = :importer " +
            "AND bef.status = 'NEW'"
    )
    List<BirthExtractFull> findByProjectAndImporterAndStatusNew(Project project, User importer);


    @Query("SELECT bef " +
            "FROM BirthExtractFull AS bef " +
            "JOIN ProjectNumberBookFile AS pnbf " +
            "ON bef.projectNumberBookFile = pnbf " +
            "WHERE bef.project = :project " +
            "AND bef.importer = :importer " +
            "AND bef.status = 'LATER_PROCESSING'"
    )
    List<BirthExtractFull> findByProjectAndImporterAndStatusLater(Project project, User importer);


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
    List<BirthExtractFull> findByProjectAndImporterAndStatusImported(Project project, User importer);


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
    List<BirthExtractFull> findBirthSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
