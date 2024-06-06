package com.tst.services.accessPointHistory;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse;
import com.tst.models.responses.report.TotalAccessPointHistoryResponse;
import com.tst.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IAccessPointHistoryService extends IGeneralService<AccessPointHistory, Long> {

    Optional<AccessPointHistory> findByProjectAndAccessPointAndAssignees(
            Project project,
            AccessPoint accessPoint,
            User assignees
    );

    List<ImporterAccessPointHistoryResponse> findAllNotDoneByProjectAndAssignees(
            Project project,
            User assignees
    );

    List<TotalAccessPointHistoryResponse> findAllNotDoneByProject(Project project);

    List<TotalAccessPointHistoryResponse> findAllNotDoneByProjectAndAccessPoint(Project project, AccessPoint accessPoint);
}
