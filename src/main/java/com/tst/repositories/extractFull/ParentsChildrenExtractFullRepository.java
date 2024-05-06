package com.tst.repositories.extractFull;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import com.tst.models.enums.EInputStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParentsChildrenExtractFullRepository extends JpaRepository<ParentsChildrenExtractFull, Long> {

    List<ParentsChildrenExtractFull> findByProjectAndImporterIsNull(Project project);

    Optional<ParentsChildrenExtractFull> findByProjectNumberBookFileAndStatusAndImporterIsNotNull(ProjectNumberBookFile projectNumberBookFile, EInputStatus status);


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
    List<ParentsChildrenExtractFull> findParentsChildrenSameByAccessPointAndStatusNewOrLater(AccessPoint accessPoint);

}
