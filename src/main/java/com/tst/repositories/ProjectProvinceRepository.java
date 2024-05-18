package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProjectProvinceRepository extends JpaRepository<ProjectProvince, Long> {

    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(pp.a0), " +
                "SUM(pp.a1), " +
                "SUM(pp.a2), " +
                "SUM(pp.a3), " +
                "SUM(pp.a4), " +
                "SUM(pp.convertA4), " +
                "SUM(pp.totalSize) " +
            ") " +
            "FROM ProjectProvince pp " +
            "WHERE pp.project = :project"
    )
    PaperSizeDTO findByProject(Project project);


    Optional<ProjectProvince> findByProjectAndCode(Project project, String code);

}
