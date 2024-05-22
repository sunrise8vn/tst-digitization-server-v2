package com.tst.repositories;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.responses.project.NumberBookPendingResponse;
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


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookPendingResponse (" +
                "pnb.id, " +
                "pp.name, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.projectNumberBookCover.folderPath, " +
                "pnb.projectNumberBookCover.fileName, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'NEW' " +
            "AND pnb.id = :id " +
            "AND pnb.project = :project " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "JOIN ProjectProvince AS pp " +
            "ON pd.projectProvince = pp "
    )
    Optional<NumberBookPendingResponse> findNewByProjectAndId(
            Project project,
            Long id
    );

    @Query(value = "CALL sp_find_next_new_number_book_by_id(:projectId, :userId, :id)", nativeQuery = true)
    Optional<ProjectNumberBook> findNextNewByProjectAndUserIdAndId(
            Long projectId,
            String userId,
            Long id
    );


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
