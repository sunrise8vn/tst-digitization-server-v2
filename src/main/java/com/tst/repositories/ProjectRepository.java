package com.tst.repositories;

import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.responses.project.ProjectResponse;
import com.tst.models.responses.project.RegistrationPointResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT NEW com.tst.models.responses.project.ProjectResponse (" +
                "prj.id, " +
                "prj.name, " +
                "prj.description" +
            ") " +
            "FROM Project AS prj " +
            "JOIN ProjectUser AS pu " +
            "ON pu.project = prj " +
            "AND pu.user = :user " +
            "AND prj = :project"
    )
    Optional<ProjectResponse> findProjectResponseByProjectAndUser(Project project, User user);


    @Query("SELECT NEW com.tst.models.responses.project.RegistrationPointResponse (" +
                "pw.id, " +
                "prj.name, " +
                "pp.name, " +
                "pd.name, " +
                "pw.name" +
            ") " +
            "FROM ProjectWard AS pw " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "JOIN ProjectProvince AS pp " +
            "ON pd.projectProvince = pp " +
            "JOIN Project AS prj " +
            "ON pp.project = prj " +
            "JOIN ProjectUser AS pu " +
            "ON pu.project = prj " +
            "WHERE prj = :project " +
            "AND pu.user = :user"
    )
    List<RegistrationPointResponse> findAllRegistrationPointByProjectAndUser(Project project, User user);
}
