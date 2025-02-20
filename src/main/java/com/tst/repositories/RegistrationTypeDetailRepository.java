package com.tst.repositories;

import com.tst.models.entities.RegistrationTypeDetail;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.typeList.RegistrationTypeDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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


    @Query("SELECT NEW com.tst.models.responses.typeList.RegistrationTypeDetailResponse (" +
                "rtd.code, " +
                "rtd.name" +
            ") " +
            "FROM RegistrationTypeDetail AS rtd " +
            "JOIN RegistrationType AS rt " +
            "ON rtd.registrationType = rt " +
            "WHERE rt.code = :eRegistrationType"
    )
    List<RegistrationTypeDetailResponse> findAllRegistrationTypeDetailResponse(ERegistrationType eRegistrationType);

}
