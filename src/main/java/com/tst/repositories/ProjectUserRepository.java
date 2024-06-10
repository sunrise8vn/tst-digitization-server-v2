package com.tst.repositories;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectResponse;
import com.tst.models.responses.user.UserAssignResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    Optional<ProjectUser> findByProjectAndUser(Project project, User user);


    @Query("SELECT NEW com.tst.models.responses.project.ProjectResponse (" +
                "prj.id, " +
                "prj.name, " +
                "prj.description" +
            ") " +
            "FROM ProjectUser AS prju " +
            "LEFT JOIN Project AS prj " +
            "ON prju.project = prj " +
            "WHERE prju.user = :user"
    )
    List<ProjectResponse> findAllProjectResponseByUser(User user);


    @Query("SELECT NEW com.tst.models.responses.user.UserAssignResponse (" +
                "us.id, " +
                "us.username, " +
                "CASE WHEN ui.fullName IS NULL THEN us.username ELSE ui.fullName END" +
            ") " +
            "FROM ProjectUser AS pu " +
            "JOIN User AS us " +
            "ON pu.user = us " +
            "AND pu.project = :project " +
            "LEFT JOIN UserInfo AS ui " +
            "ON ui.user = us"
    )
    List<UserAssignResponse> findAllByProject(Project project);


    @Query("SELECT NEW com.tst.models.responses.user.UserAssignResponse (" +
                "us.id, " +
                "us.username, " +
                "CASE WHEN ui.fullName IS NULL THEN us.username ELSE ui.fullName END" +
            ") " +
            "FROM AccessPointHistory AS aph " +
            "JOIN User AS us " +
            "ON aph.assignees = us " +
            "AND aph.project = :project " +
            "AND aph.accessPoint = :accessPoint " +
            "JOIN AccessPoint AS ap " +
            "ON aph.accessPoint = ap " +
            "LEFT JOIN UserInfo AS ui " +
            "ON ui.user = us"
    )
    List<UserAssignResponse> findAllByProjectAndAccessPoint(Project project, AccessPoint accessPoint);



}
