package com.tst.services.projectWard;

import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.entities.ProjectWard;
import com.tst.services.IGeneralService;

public interface IProjectWardService extends IGeneralService<ProjectWard, Long> {

    void updatePaperCountSize(ProjectWard projectWard);

}
