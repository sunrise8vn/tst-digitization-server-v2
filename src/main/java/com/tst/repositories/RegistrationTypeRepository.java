package com.tst.repositories;

import com.tst.models.entities.RegistrationType;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.typeList.RegistrationTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistrationTypeRepository extends JpaRepository<RegistrationType, Long> {

    Optional<RegistrationType> findByCode(ERegistrationType code);

    @Query("SELECT NEW com.tst.models.responses.typeList.RegistrationTypeResponse (" +
                "rt.code, " +
                "rt.name" +
            ") " +
            "FROM RegistrationType AS rt")
    List<RegistrationTypeResponse> findAllRegistrationTypeResponse();
}
