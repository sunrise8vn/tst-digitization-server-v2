package com.tst.services.exportHistory;

import com.tst.models.entities.ExportHistory;
import com.tst.models.entities.ProjectWard;
import com.tst.models.entities.User;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.ERegistrationType;
import com.tst.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IExportHistoryService extends IGeneralService<ExportHistory, Long> {

    Optional<ExportHistory> findByAll(
            Long projectId,
            Long wardId,
            String registrationType,
            String paperSize,
            String numberBookYear,
            String numberBook,
            EExportStatus status
    );

    List<ExportHistory> findAllByProvinceIdOrderByIdDesc(Long provinceId);

    List<ExportHistory> findAllByDistrictIdOrderByIdDesc(Long districtId);

    List<ExportHistory> findAllByWardIdOrderByIdDesc(Long wardId);

    void asyncExportExcel(
            ProjectWard projectWard,
            ERegistrationType registrationType,
            EPaperSize paperSize,
            String numberBookYear,
            String numberBook,
            User user
    );
}
