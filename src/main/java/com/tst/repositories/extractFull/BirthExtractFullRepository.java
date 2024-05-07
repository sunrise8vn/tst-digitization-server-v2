package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.BirthExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BirthExtractFullRepository extends JpaRepository<BirthExtractFull, Long> {

    List<BirthExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<BirthExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


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


    Optional<BirthExtractFull> findByIdAndStatus(Long id, EInputStatus status);

}
