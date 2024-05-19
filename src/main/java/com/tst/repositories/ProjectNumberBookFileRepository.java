package com.tst.repositories;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.ProjectWard;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.responses.project.NumberBookFileListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectNumberBookFileRepository extends JpaRepository<ProjectNumberBookFile, Long> {

    Boolean existsByFileNameAndProjectNumberBook(String fileName, ProjectNumberBook projectNumberBook);

    Long countByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookFileStatus status);


    @Query("SELECT SUM(pnbf.fileSize) " +
            "FROM ProjectNumberBookFile pnbf " +
            "WHERE pnbf.status = :status " +
            "AND pnbf.projectNumberBook = :projectNumberBook"
    )
    Long findTotalFileSizeByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookFileStatus status);


    Optional<ProjectNumberBookFile> findByIdAndStatus(Long id, EProjectNumberBookFileStatus status);


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


    @Query("SELECT NEW com.tst.models.responses.project.NumberBookFileListResponse (" +
                "pnbf.id, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnbf.fileName" +
            ") " +
            "FROM ProjectNumberBookFile AS pnbf " +
            "JOIN ProjectNumberBook AS pnb " +
            "ON pnbf.projectNumberBook = pnb " +
            "AND pnbf.status = :status " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw = :projectWard"
    )
    List<NumberBookFileListResponse> findAllNumberBookFileByStatus(
            ProjectWard projectWard,
            EProjectNumberBookFileStatus status
    );

}
