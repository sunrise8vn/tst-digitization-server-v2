package com.tst.repositories;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectNumberBookFileRepository extends JpaRepository<ProjectNumberBookFile, Long> {

    Optional<ProjectNumberBookFile> findByIdAndStatus(Long id, EProjectNumberBookFileStatus status);

    Boolean existsByFileNameAndProjectNumberBook(String fileName, ProjectNumberBook projectNumberBook);


    @Query("SELECT pnbf " +
            "FROM ProjectNumberBookFile AS pnbf " +
            "LEFT JOIN ProjectNumberBook AS pnb " +
            "ON pnbf.projectNumberBook = pnb " +
            "LEFT JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "LEFT JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "LEFT JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "WHERE pnbf.id = :id " +
            "AND pnbf.status = :status " +
            "AND prt.code = :registrationTypeCode"
    )
    Optional<ProjectNumberBookFile> findByIdAndRegistrationTypeCodeAndStatus(
            Long id, String registrationTypeCode, EProjectNumberBookFileStatus status
    );

    Long countByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookFileStatus status);


    @Query("SELECT SUM(pnbf.fileSize) " +
            "FROM ProjectNumberBookFile pnbf " +
            "WHERE pnbf.status = :status " +
            "AND pnbf.projectNumberBook = :projectNumberBook"
    )
    Long findTotalFileSizeByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookFileStatus status);

}
