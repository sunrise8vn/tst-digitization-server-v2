package com.tst.services.projectDistrict;

import com.tst.models.entities.ProjectDistrict;
import com.tst.services.IGeneralService;

public interface IProjectDistrictService extends IGeneralService<ProjectDistrict, Long> {

    void updatePaperCountSize(ProjectDistrict projectDistrict);

}
