package com.tst.repositories;

import com.tst.models.entities.GenderType;
import com.tst.models.responses.typeList.GenderTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenderTypeRepository extends JpaRepository<GenderType, Long> {

    Optional<GenderType> findByCode(String code);

    @Query("SELECT NEW com.tst.models.responses.typeList.GenderTypeResponse (" +
                "gt.code, " +
                "gt.name" +
            ") " +
            "FROM GenderType AS gt"
    )
    List<GenderTypeResponse> findAllGenderTypeResponse();
}
