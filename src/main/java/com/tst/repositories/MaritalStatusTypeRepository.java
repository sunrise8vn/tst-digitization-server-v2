package com.tst.repositories;

import com.tst.models.entities.MaritalStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaritalStatusTypeRepository extends JpaRepository<MaritalStatusType, Long> {

    Optional<MaritalStatusType> findByCode(String code);
}
