package com.tst.services.projectRegistrationDate;

import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.responses.project.ProjectRegistrationDateResponse;
import com.tst.models.responses.statistic.StatisticProjectNumberBookResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IProjectRegistrationDateService extends IGeneralService<ProjectRegistrationDate, Long> {

    List<ProjectRegistrationDateResponse> findAllByProjectPaperSize(ProjectPaperSize projectPaperSize);

    List<StatisticProjectNumberBookResponse> findAllStatisticByProjectPaperSize(
            ProjectPaperSize projectPaperSize
    );

    void updatePaperCountSize(ProjectRegistrationDate projectRegistrationDate);
}
