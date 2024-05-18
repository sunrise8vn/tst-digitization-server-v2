package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectDistrict;
import com.tst.models.entities.ProjectProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProjectDistrictRepository extends JpaRepository<ProjectDistrict, Long> {

    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(pd.a0), " +
                "SUM(pd.a1), " +
                "SUM(pd.a2), " +
                "SUM(pd.a3), " +
                "SUM(pd.a4), " +
                "SUM(pd.convertA4), " +
                "SUM(pd.totalSize) " +
            ") " +
            "FROM ProjectDistrict pd " +
            "WHERE pd.projectProvince = :projectProvince"
    )
    PaperSizeDTO findByProjectProvince(ProjectProvince projectProvince);


    Optional<ProjectDistrict> findByProjectAndProjectProvinceAndCode(
            Project project,
            ProjectProvince projectProvince,
            String code
    );
}
