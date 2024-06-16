package com.tst.services.exporter;

import com.tst.models.entities.ExportHistory;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.ExportHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class ZipPdfExportService implements IZipPdfExportService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);

    @Value("${server.root-folder}")
    private String serverRootFolder;

    @Value("${server.zip-folder}")
    private String zipFolder;

    private final ExportHistoryRepository exportHistoryRepository;


    @Override
    public void exportToZipPdf(
            List<?> list,
            ERegistrationType registrationType,
            String fileName,
            String sheetName,
            ExportHistory exportHistory,
            String pdfFolder
    ) {
        try (Stream<Path> stream = Files.walk(Paths.get(serverRootFolder + "/" + pdfFolder))) {
            List<Path> pdfPaths = stream
                    .filter(path -> path.getFileName().toString().endsWith(".pdf"))
                    .toList();

            // Tạo tệp ZIP
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                for (Path pdfPath : pdfPaths) {
                    zipOutputStream.putNextEntry(new ZipEntry(pdfPath.getFileName().toString()));
                    Files.copy(pdfPath, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
            }

            // Lấy kích thước của tệp MB
            long zipFileSize = byteArrayOutputStream.size() / 1024 / 1024;

            String zipFolderPath = serverRootFolder + "/" + zipFolder;
            File zipFolderFile = new File(zipFolderPath);

            if (!zipFolderFile.exists()) {
                boolean folderCreated = zipFolderFile.mkdirs();
                if (!folderCreated) {
                    logger.error("Không thể tạo thư mục zip: " + zipFolderPath);
                    exportHistory.setStatus(EExportStatus.FAILED);
                    exportHistoryRepository.saveAndFlush(exportHistory);
                    return;
                }
            }

            // Lưu tệp ZIP vào thư mục
            String zipFilePath = zipFolderPath + "/" + fileName;
            Files.write(Paths.get(zipFilePath), byteArrayOutputStream.toByteArray());

            exportHistory.setZipSize(zipFileSize);
            exportHistory.setStatus(EExportStatus.SUCCESS);
            exportHistoryRepository.save(exportHistory);
        } catch (IOException e) {
            logger.error("Đã xảy ra lỗi khi xử lý tệp: " + e.getMessage());
            exportHistory.setStatus(EExportStatus.FAILED);
            exportHistoryRepository.saveAndFlush(exportHistory);
        }
    }
}
