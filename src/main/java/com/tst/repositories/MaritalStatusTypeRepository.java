package com.tst.repositories;

import com.tst.models.entities.MaritalStatusType;
import com.tst.models.responses.typeList.MaritalStatusResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaritalStatusTypeRepository extends JpaRepository<MaritalStatusType, Long> {

    Optional<MaritalStatusType> findByCode(String code);


    @Query("SELECT NEW com.tst.models.responses.typeList.MaritalStatusResponse (" +
                "mst.code, " +
                "mst.name" +
            ") " +
            "FROM MaritalStatusType AS mst"
    )
    List<MaritalStatusResponse> findAllMaritalStatusResponse();
}
