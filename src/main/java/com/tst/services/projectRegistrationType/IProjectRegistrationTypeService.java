package com.tst.services.projectRegistrationType;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.services.IGeneralService;

public interface IProjectRegistrationTypeService extends IGeneralService<ProjectRegistrationType, Long> {

    void updatePaperCountSize(ProjectRegistrationType projectRegistrationType);

}
