package com.tst.services.projectWard;

import com.tst.models.entities.ProjectDistrict;
import com.tst.models.entities.ProjectWard;
import com.tst.models.responses.statistic.StatisticProjectWardResponse;
import com.tst.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IProjectWardService extends IGeneralService<ProjectWard, Long> {

    Optional<ProjectWard> findByIdAndProjectId(Long id, Long projectId);

    List<StatisticProjectWardResponse> findAllStatisticByProjectDistrict(
            ProjectDistrict projectDistrict
    );

    void updatePaperCountSize(ProjectWard projectWard);

}
