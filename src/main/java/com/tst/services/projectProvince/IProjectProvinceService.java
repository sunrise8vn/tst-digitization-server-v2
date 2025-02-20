package com.tst.services.projectProvince;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectProvince;
import com.tst.models.responses.statistic.StatisticProjectProvinceResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IProjectProvinceService extends IGeneralService<ProjectProvince, Long> {

    List<StatisticProjectProvinceResponse> findAllStatisticByProject(
            Project project
    );

    void updatePaperCountSize(ProjectProvince projectProvince);

}
