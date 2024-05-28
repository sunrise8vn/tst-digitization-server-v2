package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectDistrict;
import com.tst.models.entities.ProjectWard;
import com.tst.models.responses.statistic.StatisticProjectWardResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectWardRepository extends JpaRepository<ProjectWard, Long> {

    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(pw.a0), " +
                "SUM(pw.a1), " +
                "SUM(pw.a2), " +
                "SUM(pw.a3), " +
                "SUM(pw.a4), " +
                "SUM(pw.convertA4), " +
                "SUM(pw.totalSize) " +
            ") " +
            "FROM ProjectWard pw " +
            "WHERE pw.projectDistrict = :projectDistrict"
    )
    PaperSizeDTO findByProjectDistrict(ProjectDistrict projectDistrict);


    Optional<ProjectWard> findByProjectAndProjectDistrictAndCode(
            Project project,
            ProjectDistrict projectDistrict,
            String code
    );


    @Query("SELECT pw " +
            "FROM ProjectWard AS pw " +
            "WHERE pw.id = :id " +
            "AND pw.project.id = :projectId"
    )
    Optional<ProjectWard> findByIdAndProjectId(Long id, Long projectId);


    @Query("SELECT NEW com.tst.models.responses.statistic.StatisticProjectWardResponse (" +
                "pw.id, " +
                "pw.name, " +
                "(pw.a0 + pw.a1 + pw.a2 + pw.a3 + pw.a4), " +
                "pw.totalSize, " +
                "pw.a0, " +
                "pw.a1, " +
                "pw.a2, " +
                "pw.a3, " +
                "pw.a4, " +
                "pw.convertA4 " +
            ") " +
            "FROM ProjectWard AS pw " +
            "WHERE pw.projectDistrict = :projectDistrict "
    )
    List<StatisticProjectWardResponse> findAllStatisticByProjectDistrict(
            ProjectDistrict projectDistrict
    );

}
