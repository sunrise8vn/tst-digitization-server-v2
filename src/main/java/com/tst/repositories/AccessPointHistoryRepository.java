package com.tst.repositories;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse;
import com.tst.models.responses.report.TotalAccessPointHistoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccessPointHistoryRepository extends JpaRepository<AccessPointHistory, Long> {

    List<AccessPointHistory> findByAccessPoint(AccessPoint accessPoint);

    AccessPointHistory getByAccessPointAndAssignees(AccessPoint accessPoint, User assignees);


    Optional<AccessPointHistory> findByProjectAndAccessPointAndAssignees(
            Project project,
            AccessPoint accessPoint,
            User assignees
    );


    @Query("SELECT NEW com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse (" +
                "aph.id, " +
                "aph.createdAt, " +
                "aph.countExtractShort, " +
                "aph.countExtractFull, " +
                "aph.countSuccessExtractShort, " +
                "aph.countSuccessExtractFull, " +
                "aph.countErrorExtractShort, " +
                "aph.countErrorExtractFull, " +
                "aph.countRevokeExtractShort, " +
                "aph.countRevokeExtractFull, " +
                "aph.totalCount" +
            ") " +
            "FROM AccessPointHistory AS aph " +
            "JOIN AccessPoint AS ap " +
            "ON aph.accessPoint = ap " +
            "AND ap.project = :project " +
            "AND aph.assignees = :assignees " +
            "AND ap.status <> 'DONE' "
    )
    List<ImporterAccessPointHistoryResponse> findAllNotDoneByProjectAndAssignees(
            Project project,
            User assignees
    );


    @Query("SELECT NEW com.tst.models.responses.report.TotalAccessPointHistoryResponse (" +
                "ap.id, " +
                "aph.createdAt, " +
                "CASE WHEN ui.fullName IS NULL THEN us.username ELSE ui.fullName END, " +
                "aph.countExtractShort, " +
                "aph.countExtractFull, " +
                "aph.countSuccessExtractShort, " +
                "aph.countSuccessExtractFull, " +
                "aph.countErrorExtractShort, " +
                "aph.countErrorExtractFull, " +
                "aph.countRevokeExtractShort, " +
                "aph.countRevokeExtractFull, " +
                "aph.totalCount" +
            ") " +
            "FROM AccessPointHistory AS aph " +
            "JOIN AccessPoint AS ap " +
            "ON aph.accessPoint = ap " +
            "AND ap.project = :project " +
            "AND ap.status <> 'DONE' " +
            "JOIN User AS us " +
            "ON aph.assignees = us " +
            "LEFT JOIN UserInfo AS ui " +
            "ON ui.user = us " +
            "ORDER BY ap.id"
    )
    List<TotalAccessPointHistoryResponse> findAllNotDoneByProject(Project project);


    @Query("SELECT NEW com.tst.models.responses.report.TotalAccessPointHistoryResponse (" +
                "ap.id, " +
                "aph.createdAt, " +
                "CASE WHEN ui.fullName IS NULL THEN us.username ELSE ui.fullName END, " +
                "aph.countExtractShort, " +
                "aph.countExtractFull, " +
                "aph.countSuccessExtractShort, " +
                "aph.countSuccessExtractFull, " +
                "aph.countErrorExtractShort, " +
                "aph.countErrorExtractFull, " +
                "aph.countRevokeExtractShort, " +
                "aph.countRevokeExtractFull, " +
                "aph.totalCount" +
            ") " +
            "FROM AccessPointHistory AS aph " +
            "JOIN AccessPoint AS ap " +
            "ON aph.accessPoint = ap " +
            "AND ap.project = :project " +
            "AND aph.accessPoint = :accessPoint " +
            "AND ap.status <> 'DONE' " +
            "JOIN User AS us " +
            "ON aph.assignees = us " +
            "LEFT JOIN UserInfo AS ui " +
            "ON ui.user = us " +
            "ORDER BY ap.id"
    )
    List<TotalAccessPointHistoryResponse> findAllNotDoneByProjectAndAccessPoint(Project project, AccessPoint accessPoint);


    @Modifying(clearAutomatically=true)
    @Query("UPDATE AccessPointHistory AS aph " +
            "SET aph.countErrorExtractShort = aph.countErrorExtractShort - 1 " +
            "WHERE aph.assignees = :user " +
            "AND aph.accessPoint = :accessPoint"
    )
    void minusCountExtractShortError(@Param("accessPoint") AccessPoint accessPoint, @Param("user") User user);


    @Modifying(clearAutomatically=true)
    @Query("UPDATE AccessPointHistory AS aph " +
            "SET aph.countErrorExtractFull = aph.countErrorExtractFull - 1 " +
            "WHERE aph.assignees = :user " +
            "AND aph.accessPoint = :accessPoint"
    )
    void minusCountExtractFullError(AccessPoint accessPoint, User user);



    @Query(value = "CALL sp_find_all_extract_form_imported_for_manager(:projectId)", nativeQuery = true)
    List<Object[]> findAllExtractFormImportedForManager(Long projectId);

    @Query(value = "CALL sp_find_all_extract_form_imported_by_access_point_for_manager(:projectId, :accessPointId)", nativeQuery = true)
    List<Object[]> findAllExtractFormImportedByAccessPointForManager(Long projectId, Long accessPointId);

}
