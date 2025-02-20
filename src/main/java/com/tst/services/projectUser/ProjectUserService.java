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

import java.util.*;
import java.util.stream.Collectors;

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
    public List<UserAssignResponse> findAllUserAssignByProject(Project project) {
        return projectUserRepository.findAllUserAssignByProject(project);
    }

    @Override
    public List<UserAssignResponse> findAllByProjectAndAccessPoint(Project project, AccessPoint accessPoint) {
        return projectUserRepository.findAllByProjectAndAccessPoint(project, accessPoint);
    }

    @Override
    @Transactional
    public void updateUsers(Project project, List<User> newUsers) {
        List<ProjectUser> existingProjectUsers = projectUserRepository.findAllByProject(project);

        // Tạo danh sách các users hiện có
        List<User> existingUsers = existingProjectUsers.stream()
                .map(ProjectUser::getUser)
                .toList();

        // Tạo danh sách các users cần xóa
        List<User> usersToDelete = existingUsers.stream()
                .filter(user -> !newUsers.contains(user))
                .toList();

        // Tạo danh sách các users cần thêm mới
        List<User> usersToAdd = newUsers.stream()
                .filter(user -> !existingUsers.contains(user))
                .toList();

        // Xóa các users cũ
        projectUserRepository.deleteAllByProjectAndUserIn(project, usersToDelete);

        // Thêm mới các users mới
        List<ProjectUser> newProjectUsers = usersToAdd.stream()
                .map(user -> new ProjectUser().setProject(project).setUser(user))
                .collect(Collectors.toList());

        projectUserRepository.saveAll(newProjectUsers);
    }

    @Override
    public void delete(ProjectUser projectUser) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
