package com.tst.repositories;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessPointHistoryRepository extends JpaRepository<AccessPointHistory, Long> {

    List<AccessPointHistory> findByAccessPoint(AccessPoint accessPoint);

    AccessPointHistory getByAccessPointAndAssignees(AccessPoint accessPoint, User assignees);
}
