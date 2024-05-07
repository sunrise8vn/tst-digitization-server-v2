package com.tst.repositories;

import com.tst.models.entities.ResidenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResidenceTypeRepository extends JpaRepository<ResidenceType, Long> {

    Optional<ResidenceType> findByCode(String code);
}
