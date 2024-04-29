package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectDistrict;
import com.tst.models.entities.ProjectWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
