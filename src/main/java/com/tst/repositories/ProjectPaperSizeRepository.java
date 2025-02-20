package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.enums.EPaperSize;
import com.tst.models.responses.project.ProjectPaperSizeResponse;
import com.tst.models.responses.statistic.StatisticProjectNumberBookResponse;
import com.tst.models.responses.statistic.StatisticProjectPaperSizeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectPaperSizeRepository extends JpaRepository<ProjectPaperSize, Long> {

    Optional<ProjectPaperSize> findByProjectRegistrationTypeAndCode(ProjectRegistrationType projectRegistrationType, EPaperSize code);


    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(pps.a0), " +
                "SUM(pps.a1), " +
                "SUM(pps.a2), " +
                "SUM(pps.a3), " +
                "SUM(pps.a4), " +
                "SUM(pps.convertA4), " +
                "SUM(pps.totalSize) " +
            ") " +
            "FROM ProjectPaperSize pps " +
            "WHERE pps.projectRegistrationType = :projectRegistrationType"
    )
    PaperSizeDTO findByProjectRegistrationType(ProjectRegistrationType projectRegistrationType);


    @Query("SELECT NEW com.tst.models.responses.project.ProjectPaperSizeResponse (" +
                "pps.id, " +
                "pps.code" +
            ") " +
            "FROM ProjectPaperSize AS pps " +
            "WHERE pps.projectRegistrationType = :projectRegistrationType"
    )
    List<ProjectPaperSizeResponse> findAllByProjectRegistrationType(ProjectRegistrationType projectRegistrationType);


    @Query("SELECT NEW com.tst.models.responses.statistic.StatisticProjectPaperSizeResponse (" +
                "pps.id, " +
                "pps.code, " +
                "(pps.a0 + pps.a1 + pps.a2 + pps.a3 + pps.a4), " +
                "pps.totalSize, " +
                "pps.a0, " +
                "pps.a1, " +
                "pps.a2, " +
                "pps.a3, " +
                "pps.a4, " +
                "pps.convertA4 " +
            ") " +
            "FROM ProjectPaperSize AS pps " +
            "WHERE pps.projectRegistrationType = :projectRegistrationType "
    )
    List<StatisticProjectPaperSizeResponse> findAllStatisticByProjectRegistrationType(
            ProjectRegistrationType projectRegistrationType
    );

}
