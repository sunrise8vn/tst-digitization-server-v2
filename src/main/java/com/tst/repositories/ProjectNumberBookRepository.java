package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.enums.EProjectNumberBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProjectNumberBookRepository extends JpaRepository<ProjectNumberBook, Long> {

    Boolean existsByProjectRegistrationDateAndCodeAndStatus(
            ProjectRegistrationDate projectRegistrationDate,
            String code,
            EProjectNumberBookStatus status
    );

    Optional<ProjectNumberBook> findByIdAndStatus(Long id, EProjectNumberBookStatus status);

    Optional<ProjectNumberBook> findByProjectRegistrationDateAndCodeAndStatusNot(
            ProjectRegistrationDate projectRegistrationDate,
            String code,
            EProjectNumberBookStatus status
    );


    Optional<ProjectNumberBook> findByProjectAndId(Project project, Long id);


    @Query("SELECT NEW com.tst.models.dtos.project.PaperSizeDTO(" +
                "SUM(pnb.a0), " +
                "SUM(pnb.a1), " +
                "SUM(pnb.a2), " +
                "SUM(pnb.a3), " +
                "SUM(pnb.a4), " +
                "SUM(pnb.convertA4), " +
                "SUM(pnb.totalSize) " +
            ") " +
            "FROM ProjectNumberBook pnb " +
            "WHERE pnb.projectRegistrationDate = :projectRegistrationDate"
    )
    PaperSizeDTO findByProjectRegistrationDate(ProjectRegistrationDate projectRegistrationDate);

}
