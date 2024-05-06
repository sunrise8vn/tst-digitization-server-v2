package com.tst.repositories.extractShort;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentsChildrenExtractShortRepository extends JpaRepository<ParentsChildrenExtractShort, Long> {

    List<ParentsChildrenExtractShort> findByProjectAndImporterIsNull(Project project);

    List<ParentsChildrenExtractShort> findByAccessPointAndStatusAndImporterIsNotNull(AccessPoint accessPoint, EInputStatus status);


    @Query("SELECT pces " +
            "FROM ParentsChildrenExtractShort AS pces " +
            "JOIN ParentsChildrenExtractFull AS pcef " +
            "ON pces.projectNumberBookFile = pcef.projectNumberBookFile " +
            "WHERE pces.accessPoint = :accessPoint " +
            "AND pces.status = pcef.status " +
            "AND (pces.status = 'NEW' " +
            "OR pces.status = 'LATER_PROCESSING')"
    )
    List<ParentsChildrenExtractShort> findParentsChildrenSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
