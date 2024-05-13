package com.tst.services.projectUser;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectResponse;
import com.tst.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IProjectUserService extends IGeneralService<ProjectUser, Long> {

    Optional<ProjectUser> findByProjectAndUser(Project project, User user);

    List<ProjectResponse> findAllProjectResponseByUser(User user);

}
