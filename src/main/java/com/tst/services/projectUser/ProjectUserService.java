package com.tst.services.projectUser;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectByUserResponse;
import com.tst.models.responses.user.UserAssignResponse;
import com.tst.repositories.ProjectUserRepository;
import com.tst.services.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectUserService implements IProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    private final BatchService batchService;


    @Override
    public Optional<ProjectUser> findById(Long id) {
        return projectUserRepository.findById(id);
    }

    @Override
    public Optional<ProjectUser> findByProjectAndUser(Project project, User user) {
        return projectUserRepository.findByProjectAndUser(project, user);
    }

    @Override
    public List<ProjectByUserResponse> findAllProjectsByUserResponse(User user) {
        return projectUserRepository.findAllProjectsByUserResponse(user);
    }

    @Override
    public List<UserAssignResponse> findAllByProject(Project project) {
        return projectUserRepository.findAllByProject(project);
    }

    @Override
    public List<UserAssignResponse> findAllByProjectAndAccessPoint(Project project, AccessPoint accessPoint) {
        return projectUserRepository.findAllByProjectAndAccessPoint(project, accessPoint);
    }

    @Override
    @Transactional
    public void updateUsers(Project project, List<User> users) {
        projectUserRepository.deleteAllByProject(project);

        List<ProjectUser> projectUsers = new ArrayList<>();

        for (User user : users) {
            ProjectUser projectUser = new ProjectUser()
                    .setProject(project)
                    .setUser(user);

            projectUsers.add(projectUser);
        }

        batchService.batchCreate(projectUsers);
    }

    @Override
    public void delete(ProjectUser projectUser) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
