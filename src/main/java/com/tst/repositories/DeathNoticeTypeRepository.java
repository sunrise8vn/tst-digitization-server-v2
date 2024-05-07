package com.tst.repositories;

import com.tst.models.entities.DeathNoticeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeathNoticeTypeRepository extends JpaRepository<DeathNoticeType, Long> {

    Optional<DeathNoticeType> findByCode(String code);
}
