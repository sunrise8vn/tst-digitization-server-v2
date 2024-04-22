package com.tst.repositories;

import com.tst.models.entities.RegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationTypeRepository extends JpaRepository<RegistrationType, Long> {
}
