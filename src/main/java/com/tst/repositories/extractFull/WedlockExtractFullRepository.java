package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WedlockExtractFullRepository extends JpaRepository<WedlockExtractFull, Long> {

    List<WedlockExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<WedlockExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


    @Query("SELECT wef " +
            "FROM WedlockExtractFull AS wef " +
            "JOIN WedlockExtractShort AS wes " +
            "ON wef.projectNumberBookFile = wes.projectNumberBookFile " +
            "WHERE wef.accessPoint = :accessPoint " +
            "AND wef.status = wes.status " +
            "AND (wef.status = 'NEW' " +
            "OR wef.status = 'LATER_PROCESSING')"
    )
    List<WedlockExtractFull> findWedlockSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
