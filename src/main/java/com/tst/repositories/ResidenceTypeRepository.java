package com.tst.repositories;

import com.tst.models.entities.ResidenceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidenceTypeRepository extends JpaRepository<ResidenceType, Long> {
}
