package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProjectPaperSizeRepository extends JpaRepository<ProjectPaperSize, Long> {

    Optional<ProjectPaperSize> findByCode(EPaperSize code);


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
}
