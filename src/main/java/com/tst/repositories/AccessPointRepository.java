package com.tst.repositories;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.enums.EAccessPointStatus;
import com.tst.models.responses.report.AccessPointResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AccessPointRepository extends JpaRepository<AccessPoint, Long> {

    @Query("SELECT NEW com.tst.models.responses.report.AccessPointResponse (" +
                "ap.id, " +
                "CONCAT('Đợt phân phối ', ap.id)" +
            ") " +
            "FROM AccessPointHistory AS aph " +
            "JOIN AccessPoint AS ap " +
            "ON aph.accessPoint = ap " +
            "AND aph.assignees = :user " +
            "AND ap.project = :project " +
            "AND ap.status <> :status"
    )
    List<AccessPointResponse> findAllAccessPointProcessingByProjectAndUserAndStatusNot(
            Project project,
            User user,
            EAccessPointStatus status
    );


    @Query("SELECT NEW com.tst.models.responses.report.AccessPointResponse (" +
                "ap.id, " +
                "CONCAT('Đợt phân phối ', ap.id)" +
            ") " +
            "FROM AccessPoint AS ap " +
            "WHERE ap.project = :project " +
            "AND ap.status <> :status"
    )
    List<AccessPointResponse> findAllAccessPointProcessingByProjectAndStatusNot(
            Project project,
            EAccessPointStatus status
    );


    @Modifying(clearAutomatically=true)
    @Query("UPDATE AccessPoint AS ap " +
            "SET ap.totalErrorExtractShort = ap.totalErrorExtractShort - 1 " +
            "WHERE ap = :accessPoint"
    )
    void minusCountExtractShortError(AccessPoint accessPoint);


    @Modifying(clearAutomatically=true)
    @Query("UPDATE AccessPoint AS ap " +
            "SET ap.totalErrorExtractShort = ap.totalErrorExtractShort - 1 " +
            "WHERE ap = :accessPoint"
    )
    void minusCountExtractFullError(AccessPoint accessPoint);

}
