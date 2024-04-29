package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.entities.ProjectWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


    Optional<ProjectRegistrationType> findByProjectWardAndCode(ProjectWard projectWard, String code);

}
