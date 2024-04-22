package com.tst.repositories;

import com.tst.models.entities.IdentificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Long> {
}
