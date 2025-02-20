package com.tst.services.exporter;

import com.tst.models.entities.ExportHistory;
import com.tst.models.enums.ERegistrationType;

import java.util.List;

public interface IZipPdfExportService {

    void exportToZipPdf(
            List<?> list,
            ERegistrationType registrationType,
            String fileName,
            String sheetName,
            ExportHistory exportHistory,
            String pdfFolder
    );

}
