package com.tst.services.projectWard;

import com.tst.models.entities.ProjectWard;
import com.tst.services.IGeneralService;

import java.util.Optional;

public interface IProjectWardService extends IGeneralService<ProjectWard, Long> {

    Optional<ProjectWard> findByIdAndProjectId(Long id, Long projectId);

    void updatePaperCountSize(ProjectWard projectWard);

}
