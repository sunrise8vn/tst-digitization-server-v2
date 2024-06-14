package com.tst.repositories;

import com.tst.models.entities.*;
import com.tst.models.enums.EExportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExportHistoryRepository extends JpaRepository<ExportHistory, Long> {

    ExportHistory getByTs(Long ts);


    @Query("SELECT eh " +
            "FROM ExportHistory AS eh " +
            "WHERE eh.projectId = :projectId " +
            "AND eh.wardId = :wardId " +
            "AND eh.registrationType = :registrationType " +
            "AND eh.paperSize = :paperSize " +
            "AND eh.numberBookYear = :numberBookYear " +
            "AND eh.numberBook = :numberBook " +
            "AND eh.status = :status"
    )
    Optional<ExportHistory> findByAll(
            Long projectId,
            Long wardId,
            String registrationType,
            String paperSize,
            String numberBookYear,
            String numberBook,
            EExportStatus status
    );

    Optional<ExportHistory> findByExcelName(String excelName);

    Optional<ExportHistory> findByZipName(String zipName);

    List<ExportHistory> findAllByProvinceIdOrderByIdDesc(Long provinceId);

    List<ExportHistory> findAllByDistrictIdOrderByIdDesc(Long districtId);

    List<ExportHistory> findAllByWardIdOrderByIdDesc(Long wardId);
}
