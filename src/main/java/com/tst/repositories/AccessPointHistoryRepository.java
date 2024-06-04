package com.tst.repositories;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessPointHistoryRepository extends JpaRepository<AccessPointHistory, Long> {

    List<AccessPointHistory> findByAccessPoint(AccessPoint accessPoint);

    AccessPointHistory getByAccessPointAndAssignees(AccessPoint accessPoint, User assignees);


    Optional<AccessPointHistory> findByProjectAndAccessPointAndAssignees(
            Project project,
            AccessPoint accessPoint,
            User assignees
    );


    @Query("SELECT NEW com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse (" +
                "aph.id, " +
                "aph.createdAt, " +
                "aph.countExtractShort, " +
                "aph.countExtractFull, " +
                "aph.countSuccessExtractShort, " +
                "aph.countSuccessExtractFull, " +
                "aph.countErrorExtractShort, " +
                "aph.countErrorExtractFull, " +
                "aph.countRevokeExtractShort, " +
                "aph.countRevokeExtractFull, " +
                "aph.totalCount" +
            ") " +
            "FROM AccessPointHistory AS aph " +
            "JOIN AccessPoint AS ap " +
            "ON aph.accessPoint = ap " +
            "AND ap.project = :project " +
            "AND aph.assignees = :assignees " +
            "AND ap.status <> 'DONE' "
    )
    List<ImporterAccessPointHistoryResponse> findAllNotDoneByProjectAndAssignees(
            Project project,
            User assignees
    );
}
