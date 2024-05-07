package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WedlockExtractShortRepository extends JpaRepository<WedlockExtractShort, Long> {

    List<WedlockExtractShort> findByProjectAndImporterIsNull(Project project);

    List<WedlockExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT wes " +
            "FROM WedlockExtractShort AS wes " +
            "JOIN WedlockExtractFull AS wef " +
            "ON wes.projectNumberBookFile = wef.projectNumberBookFile " +
            "WHERE wes.accessPoint = :accessPoint " +
            "AND wes.status = wef.status " +
            "AND (wes.status = 'NEW' " +
            "OR wes.status = 'LATER_PROCESSING') " +
            "ORDER BY wes.id DESC"
    )
    List<WedlockExtractShort> findWedlockSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);


    Optional<WedlockExtractShort> findByIdAndStatus(Long id, EInputStatus status);

}
