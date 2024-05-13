package com.tst.repositories;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectResponse;
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
}
