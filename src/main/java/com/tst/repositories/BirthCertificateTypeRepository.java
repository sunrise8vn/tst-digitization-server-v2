package com.tst.repositories;

import com.tst.models.entities.BirthCertificateType;
import com.tst.models.responses.typeList.BirthCertificateTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BirthCertificateTypeRepository extends JpaRepository<BirthCertificateType, Long> {

    Optional<BirthCertificateType> findByCode(String code);

    @Query("SELECT NEW com.tst.models.responses.typeList.BirthCertificateTypeResponse (" +
                "bct.code, " +
                "bct.name" +
            ") " +
            "FROM BirthCertificateType AS bct"
    )
    List<BirthCertificateTypeResponse> findAllBirthCertificateTypeResponse();
}
