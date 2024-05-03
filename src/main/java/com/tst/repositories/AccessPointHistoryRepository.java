package com.tst.repositories;

import com.tst.models.entities.AccessPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessPointHistoryRepository extends JpaRepository<AccessPointHistory, Long> {
}
