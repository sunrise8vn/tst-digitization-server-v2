package com.tst.services.projectPaperSize;

import com.tst.models.entities.ProjectPaperSize;
import com.tst.services.IGeneralService;

public interface IProjectPaperSizeService extends IGeneralService<ProjectPaperSize, Long> {

    void updatePaperCountSize(ProjectPaperSize projectPaperSize);

}
