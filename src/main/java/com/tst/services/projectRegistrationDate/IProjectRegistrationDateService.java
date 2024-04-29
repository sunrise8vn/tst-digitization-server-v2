package com.tst.services.projectRegistrationDate;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.services.IGeneralService;

public interface IProjectRegistrationDateService extends IGeneralService<ProjectRegistrationDate, Long> {

    void updatePaperCountSize(ProjectRegistrationDate projectRegistrationDate);
}
