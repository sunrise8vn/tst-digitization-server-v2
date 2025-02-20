package com.tst.repositories;

import com.tst.models.entities.ConfirmationType;
import com.tst.models.responses.typeList.ConfirmationTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConfirmationTypeRepository extends JpaRepository<ConfirmationType, Long> {

    Optional<ConfirmationType> findByCode(String code);


    @Query("SELECT NEW com.tst.models.responses.typeList.ConfirmationTypeResponse (" +
                "ct.code, " +
                "ct.name" +
            ") " +
            "FROM ConfirmationType AS ct"
    )
    List<ConfirmationTypeResponse> findAllConfirmationTypeResponse();
}
