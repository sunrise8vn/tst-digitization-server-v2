package com.tst.services.projectPaperSize;

import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.responses.project.ProjectPaperSizeResponse;
import com.tst.models.responses.statistic.StatisticProjectNumberBookResponse;
import com.tst.models.responses.statistic.StatisticProjectPaperSizeResponse;
import com.tst.services.IGeneralService;

import java.util.List;

public interface IProjectPaperSizeService extends IGeneralService<ProjectPaperSize, Long> {

    List<ProjectPaperSizeResponse> findAllByProjectRegistrationType(ProjectRegistrationType projectRegistrationType);

    List<StatisticProjectPaperSizeResponse> findAllStatisticByProjectRegistrationType(
            ProjectRegistrationType projectRegistrationType
    );
    void updatePaperCountSize(ProjectPaperSize projectPaperSize);

}
