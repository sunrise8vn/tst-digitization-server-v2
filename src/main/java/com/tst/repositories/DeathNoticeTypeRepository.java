package com.tst.repositories;

import com.tst.models.entities.DeathNoticeType;
import com.tst.models.responses.typeList.DeathNoticeTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeathNoticeTypeRepository extends JpaRepository<DeathNoticeType, Long> {

    Optional<DeathNoticeType> findByCode(String code);


    @Query("SELECT NEW com.tst.models.responses.typeList.DeathNoticeTypeResponse (" +
                "dnt.code, " +
                "dnt.name" +
            ") " +
            "FROM DeathNoticeType AS dnt"
    )
    List<DeathNoticeTypeResponse> findAllDeathNoticeTypeResponse();
}
