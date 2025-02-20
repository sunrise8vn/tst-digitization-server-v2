package com.tst.repositories;

import com.tst.models.entities.IntendedUseType;
import com.tst.models.responses.typeList.IntendedUseTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IntendedUseTypeRepository extends JpaRepository<IntendedUseType, Long> {

    Optional<IntendedUseType> findByCode(String code);


    @Query("SELECT NEW com.tst.models.responses.typeList.IntendedUseTypeResponse (" +
                "iut.code, " +
                "iut.name" +
            ") " +
            "FROM IntendedUseType AS iut"
    )
    List<IntendedUseTypeResponse> findAllIntendedUseTypeResponse();
}
