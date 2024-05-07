package com.tst.repositories;

import com.tst.models.entities.BirthCertificateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BirthCertificateTypeRepository extends JpaRepository<BirthCertificateType, Long> {

    Optional<BirthCertificateType> findByCode(String code);
}
