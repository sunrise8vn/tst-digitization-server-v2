package com.tst.services.projectUser;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectResponse;
import com.tst.models.responses.user.UserAssignResponse;
import com.tst.repositories.ProjectUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectUserService implements IProjectUserService {

    private final ProjectUserRepository projectUserRepository;


    @Override
    public Optional<ProjectUser> findById(Long id) {
        return projectUserRepository.findById(id);
    }

    @Override
    public Optional<ProjectUser> findByProjectAndUser(Project project, User user) {
        return projectUserRepository.findByProjectAndUser(project, user);
    }

    @Override
    public List<ProjectResponse> findAllProjectResponseByUser(User user) {
        return projectUserRepository.findAllProjectResponseByUser(user);
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
    public void delete(ProjectUser projectUser) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
