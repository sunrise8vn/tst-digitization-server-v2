package com.tst.repositories;

import com.tst.models.entities.ConfirmationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTypeRepository extends JpaRepository<ConfirmationType, Long> {

    Optional<ConfirmationType> findByCode(String code);
}
