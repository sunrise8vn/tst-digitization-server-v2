package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
