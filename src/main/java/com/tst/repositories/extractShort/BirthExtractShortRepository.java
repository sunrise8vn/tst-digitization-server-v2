package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.BirthExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BirthExtractShortRepository extends JpaRepository<BirthExtractShort, Long> {

    List<BirthExtractShort> findByProjectAndImporterIsNull(Project project);

    List<BirthExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


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
    List<BirthExtractShort> findBirthSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);


    Optional<BirthExtractShort> findByIdAndStatus(Long id, EInputStatus status);

}
