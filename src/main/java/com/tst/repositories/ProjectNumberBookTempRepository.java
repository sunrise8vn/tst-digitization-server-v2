package com.tst.repositories;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookTemp;
import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.enums.EProjectNumberBookTempStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectNumberBookTempRepository extends JpaRepository<ProjectNumberBookTemp, Long> {

    Optional<ProjectNumberBookTemp> findByIdAndStatus(Long id, EProjectNumberBookTempStatus status);

    Boolean existsByFileNameAndProjectNumberBook(String fileName, ProjectNumberBook projectNumberBook);


    @Query("SELECT pnbt " +
            "FROM ProjectNumberBookTemp AS pnbt " +
            "LEFT JOIN ProjectNumberBook AS pnb " +
            "ON pnbt.projectNumberBook = pnb " +
            "LEFT JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "LEFT JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "LEFT JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "WHERE pnbt.id = :id " +
            "AND pnbt.status = :status " +
            "AND prt.code = :registrationTypeCode"
    )
    Optional<ProjectNumberBookTemp> findByIdAndRegistrationTypeCodeAndStatus(
            Long id, String registrationTypeCode, EProjectNumberBookTempStatus status
    );

    Long countByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookTempStatus status);


    @Query("SELECT COUNT(pnbt) " +
            "FROM ProjectNumberBookTemp pnbt " +
            "WHERE pnbt.status = :status " +
            "AND pnbt.projectNumberBook.projectRegistrationDate = :projectRegistrationDate"
    )
    Long countRegistrationDateByNumberBookAndStatus(ProjectRegistrationDate projectRegistrationDate, EProjectNumberBookTempStatus status);


    @Query("SELECT SUM(pnbt.fileSize) " +
            "FROM ProjectNumberBookTemp pnbt " +
            "WHERE pnbt.status = :status " +
            "AND pnbt.projectNumberBook = :projectNumberBook"
    )
    Long findTotalFileSizeByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookTempStatus status);


    @Query("SELECT SUM(pnbt.fileSize) " +
            "FROM ProjectNumberBookTemp pnbt " +
            "WHERE pnbt.status = :status " +
            "AND pnbt.projectNumberBook.projectRegistrationDate = :projectRegistrationDate"
    )
    Long findTotalFileSizeByProjectRegistrationDateAndStatus(ProjectRegistrationDate projectRegistrationDate, EProjectNumberBookTempStatus status);

}
