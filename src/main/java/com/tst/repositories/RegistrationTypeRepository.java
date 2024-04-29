package com.tst.repositories;

import com.tst.models.entities.RegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationTypeRepository extends JpaRepository<RegistrationType, Long> {

    Optional<RegistrationType> findByCode(String code);
}
