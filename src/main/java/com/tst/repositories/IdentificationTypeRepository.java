package com.tst.repositories;

import com.tst.models.entities.IdentificationType;
import com.tst.models.responses.typeList.IdentificationTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Long> {

    Optional<IdentificationType> findByCode(String code);


    @Query("SELECT NEW com.tst.models.responses.typeList.IdentificationTypeResponse (" +
                "it.code, " +
                "it.name" +
            ") " +
            "FROM IdentificationType AS it"
    )
    List<IdentificationTypeResponse> findAllIdentificationTypeResponse();
}
