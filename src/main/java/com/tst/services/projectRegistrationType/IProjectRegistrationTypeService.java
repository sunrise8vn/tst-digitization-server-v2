package com.tst.services.projectRegistrationType;

import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.entities.ProjectWard;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.project.ProjectRegistrationTypeResponse;
import com.tst.models.responses.statistic.StatisticProjectNumberBookResponse;
import com.tst.models.responses.statistic.StatisticProjectRegistrationTypeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IProjectRegistrationTypeService extends IGeneralService<ProjectRegistrationType, Long> {

    List<ProjectRegistrationTypeResponse> findAllByProjectWard(ProjectWard projectWard);

    List<StatisticProjectRegistrationTypeResponse> findAllStatisticByProjectWard(ProjectWard projectWard);

    void updatePaperCountSize(ProjectRegistrationType projectRegistrationType);

}
