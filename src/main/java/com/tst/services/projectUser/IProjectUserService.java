package com.tst.services.projectUser;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectByUserResponse;
import com.tst.models.responses.user.UserAssignResponse;
import com.tst.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IProjectUserService extends IGeneralService<ProjectUser, Long> {

    Optional<ProjectUser> findByProjectAndUser(Project project, User user);

    List<ProjectByUserResponse> findAllProjectsByUserResponse(User user);

    List<UserAssignResponse> findAllUserAssignByProject(Project project);

    List<UserAssignResponse> findAllByProjectAndAccessPoint(Project project, AccessPoint accessPoint);

    void updateUsers(Project project, List<User> users);

}
