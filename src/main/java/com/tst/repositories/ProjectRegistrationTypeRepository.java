package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.entities.ProjectWard;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.project.ProjectRegistrationTypeResponse;
import com.tst.models.responses.statistic.StatisticProjectRegistrationTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectRegistrationTypeRepository extends JpaRepository<ProjectRegistrationType, Long> {

    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(prt.a0), " +
                "SUM(prt.a1), " +
                "SUM(prt.a2), " +
                "SUM(prt.a3), " +
                "SUM(prt.a4), " +
                "SUM(prt.convertA4), " +
                "SUM(prt.totalSize) " +
            ") " +
            "FROM ProjectRegistrationType prt " +
            "WHERE prt.projectWard = :projectWard"
    )
    PaperSizeDTO findByProjectWard(ProjectWard projectWard);


    Optional<ProjectRegistrationType> findByProjectWardAndCode(ProjectWard projectWard, ERegistrationType code);


    @Query("SELECT NEW com.tst.models.responses.project.ProjectRegistrationTypeResponse (" +
                "prt.id, " +
                "prt.code" +
            ") " +
            "FROM ProjectRegistrationType AS prt " +
            "WHERE prt.projectWard = :projectWard"
    )
    List<ProjectRegistrationTypeResponse> findAllByProjectWard(ProjectWard projectWard);


    @Query("SELECT NEW com.tst.models.responses.statistic.StatisticProjectRegistrationTypeResponse (" +
                "prt.id, " +
                "prt.code, " +
                "(prt.a0 + prt.a1 + prt.a2 + prt.a3 + prt.a4), " +
                "prt.totalSize, " +
                "prt.a0, " +
                "prt.a1, " +
                "prt.a2, " +
                "prt.a3, " +
                "prt.a4, " +
                "prt.convertA4 " +
            ") " +
            "FROM ProjectRegistrationType AS prt " +
            "WHERE prt.projectWard = :projectWard "
    )
    List<StatisticProjectRegistrationTypeResponse> findAllStatisticByProjectWard(
            ProjectWard projectWard
    );

}
