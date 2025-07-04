package com.tst.repositories;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.ProjectWard;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.responses.project.NumberBookFileListResponse;
import com.tst.models.responses.statistic.StatictisProjectNumberBookFileResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectNumberBookFileRepository extends JpaRepository<ProjectNumberBookFile, Long> {

    Boolean existsByFileNameAndProjectNumberBook(String fileName, ProjectNumberBook projectNumberBook);

    Boolean existsByFileNameAndProjectNumberBookAndIdIsNot(String fileName, ProjectNumberBook projectNumberBook, Long id);

    Long countByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookFileStatus status);


    @Query("SELECT SUM(pnbf.fileSize) " +
            "FROM ProjectNumberBookFile pnbf " +
            "WHERE pnbf.status = :status " +
            "AND pnbf.projectNumberBook = :projectNumberBook"
    )
    Long findTotalFileSizeByProjectNumberBookAndStatus(ProjectNumberBook projectNumberBook, EProjectNumberBookFileStatus status);


    Optional<ProjectNumberBookFile> findByIdAndStatus(Long id, EProjectNumberBookFileStatus status);

    @Query(value = "CALL sp_find_next_number_book_file_by_id_and_status(:projectId, :id, :status)", nativeQuery = true)
    Optional<ProjectNumberBookFile> findNextByIdAndStatus(Long projectId, Long id, String status);


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


    @Query("SELECT NEW com.tst.models.responses.statistic.StatictisProjectNumberBookFileResponse (" +
                "pnbf.id, " +
                "pnbf.fileName, " +
                "pnbf.createdAt, " +
                "pnbf.fileSize, " +
                "CASE WHEN pnbf.paperSize = 'A0' THEN 1 ELSE 0 END, " +
                "CASE WHEN pnbf.paperSize = 'A1' THEN 1 ELSE 0 END, " +
                "CASE WHEN pnbf.paperSize = 'A2' THEN 1 ELSE 0 END, " +
                "CASE WHEN pnbf.paperSize = 'A3' THEN 1 ELSE 0 END, " +
                "CASE WHEN pnbf.paperSize = 'A4' THEN 1 ELSE 0 END, " +
                "(CASE WHEN pnbf.paperSize = 'A0' THEN 16 ELSE 0 END) + " +
                "(CASE WHEN pnbf.paperSize = 'A1' THEN 8 ELSE 0 END) + " +
                "(CASE WHEN pnbf.paperSize = 'A2' THEN 4 ELSE 0 END) + " +
                "(CASE WHEN pnbf.paperSize = 'A3' THEN 2 ELSE 0 END) + " +
                "(CASE WHEN pnbf.paperSize = 'A4' THEN 1 ELSE 0 END)" +
            ") " +
            "FROM ProjectNumberBookFile AS pnbf " +
            "WHERE pnbf.projectNumberBook = :projectNumberBook " +
            "AND pnbf.status = 'ACCEPT'"
    )
    List<StatictisProjectNumberBookFileResponse> findAllProjectNumberBookFileResponse(ProjectNumberBook projectNumberBook);


    List<ProjectNumberBookFile> findAllByProjectNumberBook(ProjectNumberBook projectNumberBook);

}
