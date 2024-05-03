package com.tst.repositories;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    Optional<ProjectUser> findByProjectAndUser(Project project, User user);
}
