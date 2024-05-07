package com.tst.repositories;

import com.tst.models.entities.GenderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderTypeRepository extends JpaRepository<GenderType, Long> {

    Optional<GenderType> findByCode(String code);
}
