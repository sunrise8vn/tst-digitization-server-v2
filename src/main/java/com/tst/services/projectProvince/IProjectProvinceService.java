package com.tst.services.projectProvince;

import com.tst.models.entities.ProjectProvince;
import com.tst.services.IGeneralService;

public interface IProjectProvinceService extends IGeneralService<ProjectProvince, Long> {

    void updatePaperCountSize(ProjectProvince projectProvince);

}
