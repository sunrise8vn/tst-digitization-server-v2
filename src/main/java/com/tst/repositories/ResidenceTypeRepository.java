package com.tst.repositories;

import com.tst.models.entities.ResidenceType;
import com.tst.models.responses.typeList.ResidenceTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResidenceTypeRepository extends JpaRepository<ResidenceType, Long> {

    Optional<ResidenceType> findByCode(String code);


    @Query("SELECT NEW com.tst.models.responses.typeList.ResidenceTypeResponse (" +
                "rt.code, " +
                "rt.name" +
            ") " +
            "FROM ResidenceType AS rt"
    )
    List<ResidenceTypeResponse> findAllResidenceTypeResponse();
}
