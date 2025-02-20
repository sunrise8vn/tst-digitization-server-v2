package com.tst.services.accessPoint;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.enums.EAccessPointStatus;
import com.tst.models.responses.report.AccessPointResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IAccessPointService extends IGeneralService<AccessPoint, Long> {

    List<AccessPointResponse> findAllAccessPointProcessingByProjectAndUserAndStatusNot(
            Project project,
            User user,
            EAccessPointStatus status
    );

    List<AccessPointResponse> findAllAccessPointProcessingByProjectAndStatusNot(
            Project project,
            EAccessPointStatus status
    );

    void revokeExtractForm(AccessPoint accessPoint, Long totalCountRevoke);

}
