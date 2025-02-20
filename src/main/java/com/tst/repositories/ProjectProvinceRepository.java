package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectProvince;
import com.tst.models.responses.statistic.StatisticProjectProvinceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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


    @Query("SELECT NEW com.tst.models.responses.statistic.StatisticProjectProvinceResponse (" +
                "pp.id, " +
                "pp.name, " +
                "(pp.a0 + pp.a1 + pp.a2 + pp.a3 + pp.a4), " +
                "pp.totalSize, " +
                "pp.a0, " +
                "pp.a1, " +
                "pp.a2, " +
                "pp.a3, " +
                "pp.a4, " +
                "pp.convertA4 " +
            ") " +
            "FROM ProjectProvince AS pp " +
            "WHERE pp.project = :project "
    )
    List<StatisticProjectProvinceResponse> findAllStatisticByProject(
            Project project
    );

}
