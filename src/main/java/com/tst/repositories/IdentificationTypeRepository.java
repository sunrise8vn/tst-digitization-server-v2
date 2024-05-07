package com.tst.repositories;

import com.tst.models.entities.IdentificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Long> {

    Optional<IdentificationType> findByCode(String code);
}
