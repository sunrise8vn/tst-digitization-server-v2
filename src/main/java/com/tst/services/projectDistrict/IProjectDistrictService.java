package com.tst.services.projectDistrict;

import com.tst.models.entities.ProjectDistrict;
import com.tst.models.entities.ProjectProvince;
import com.tst.models.responses.statistic.StatisticProjectDistrictResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IProjectDistrictService extends IGeneralService<ProjectDistrict, Long> {

    List<StatisticProjectDistrictResponse> findAllStatisticByProjectProvince(
            ProjectProvince projectProvince
    );

    void updatePaperCountSize(ProjectDistrict projectDistrict);

}
