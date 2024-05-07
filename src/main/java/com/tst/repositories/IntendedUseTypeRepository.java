package com.tst.repositories;

import com.tst.models.entities.IntendedUseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntendedUseTypeRepository extends JpaRepository<IntendedUseType, Long> {

    Optional<IntendedUseType> findByCode(String code);
}
