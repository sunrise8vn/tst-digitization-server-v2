package com.tst.repositories;

import com.tst.models.entities.RegistrationTypeDetail;
import com.tst.models.enums.ERegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegistrationTypeDetailRepository extends JpaRepository<RegistrationTypeDetail, Long> {

    @Query("SELECT rtd " +
            "FROM RegistrationTypeDetail AS rtd " +
            "JOIN RegistrationType AS rt " +
            "ON rtd.registrationType = rt " +
            "WHERE rtd.code = :code " +
            "AND rt.code = :eRegistrationType"
    )
    Optional<RegistrationTypeDetail> findByCodeAndERegistrationType(String code, ERegistrationType eRegistrationType);

}
