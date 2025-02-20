package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.responses.project.ProjectRegistrationDateResponse;
import com.tst.models.responses.statistic.StatisticProjectNumberBookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectRegistrationDateRepository extends JpaRepository<ProjectRegistrationDate, Long> {

    Optional<ProjectRegistrationDate> findByProjectPaperSizeAndCode(ProjectPaperSize projectPaperSize, String code);


    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(prd.a0), " +
                "SUM(prd.a1), " +
                "SUM(prd.a2), " +
                "SUM(prd.a3), " +
                "SUM(prd.a4), " +
                "SUM(prd.convertA4), " +
                "SUM(prd.totalSize) " +
            ") " +
            "FROM ProjectRegistrationDate prd " +
            "WHERE prd.projectPaperSize = :projectPaperSize"
    )
    PaperSizeDTO findByProjectPaperSize(ProjectPaperSize projectPaperSize);


    @Query("SELECT NEW com.tst.models.responses.project.ProjectRegistrationDateResponse (" +
                "prd.id, " +
                "prd.code" +
            ") " +
            "FROM ProjectRegistrationDate AS prd " +
            "WHERE prd.projectPaperSize = :projectPaperSize"
    )
    List<ProjectRegistrationDateResponse> findAllByProjectPaperSize(ProjectPaperSize projectPaperSize);


    @Query("SELECT NEW com.tst.models.responses.statistic.StatisticProjectNumberBookResponse (" +
                "prd.id, " +
                "prd.code, " +
                "(prd.a0 + prd.a1 + prd.a2 + prd.a3 + prd.a4), " +
                "prd.totalSize, " +
                "prd.a0, " +
                "prd.a1, " +
                "prd.a2, " +
                "prd.a3, " +
                "prd.a4, " +
                "prd.convertA4 " +
            ") " +
            "FROM ProjectRegistrationDate AS prd " +
            "WHERE prd.projectPaperSize = :projectPaperSize "
    )
    List<StatisticProjectNumberBookResponse> findAllStatisticByProjectPaperSize(
            ProjectPaperSize projectPaperSize
    );
}
