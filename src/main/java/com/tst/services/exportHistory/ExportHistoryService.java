package com.tst.services.exportHistory;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.*;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.EExportType;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.services.exporter.IExcelExportService;
import com.tst.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ExportHistoryService implements IExportHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(ExportHistoryService.class);

    @Value("${server.root-folder}")
    private String serverRootFolder;

    @Value("${server.excel-folder}")
    private String excelFolder;


    private final ExportHistoryRepository exportHistoryRepository;
    private final ParentsChildrenRepository parentsChildrenRepository;
    private final BirthRepository birthRepository;
    private final MarryRepository marryRepository;
    private final WedlockRepository wedlockRepository;
    private final DeathRepository deathRepository;

    private final IExcelExportService excelExportService;

    private final AppUtils appUtils;

    @Override
    public Optional<ExportHistory> findById(Long id) {
        return exportHistoryRepository.findById(id);
    }

    @Override
    public Optional<ExportHistory> findByAll(
            Long projectId,
            Long wardId,
            String registrationType,
            String paperSize,
            String numberBookYear,
            String numberBook,
            EExportStatus status
    ) {
        return exportHistoryRepository.findByAll(
                projectId,
                wardId,
                registrationType,
                paperSize,
                numberBookYear,
                numberBook,
                status
        );
    }

    @Override
    public List<ExportHistory> findAllByProvinceIdOrderByIdDesc(Long provinceId) {
        return exportHistoryRepository.findAllByProvinceIdOrderByIdDesc(provinceId);
    }

    @Override
    public List<ExportHistory> findAllByDistrictIdOrderByIdDesc(Long districtId) {
        return exportHistoryRepository.findAllByDistrictIdOrderByIdDesc(districtId);
    }

    @Override
    public List<ExportHistory> findAllByWardIdOrderByIdDesc(Long wardId) {
        return exportHistoryRepository.findAllByWardIdOrderByIdDesc(wardId);
    }

    @Override
    public void asyncExportExcel(
            ProjectWard projectWard,
            ERegistrationType registrationType,
            EPaperSize paperSize,
            String numberBookYear,
            String numberBook,
            User user
    ) {
        String wardName = projectWard.getCode().replace("_", "");

        String districtName = appUtils.getFirstCharactersOfEachWord(projectWard.getProjectDistrict().getName());
        districtName = appUtils.replaceNonEnglishChar(districtName);
        districtName = appUtils.removeNonAlphanumeric(districtName);

        String provinceName = appUtils.getFirstCharactersOfEachWord(projectWard.getProjectDistrict().getProjectProvince().getName());
        provinceName = appUtils.replaceNonEnglishChar(provinceName);
        provinceName = appUtils.removeNonAlphanumeric(provinceName);

        String excelName = wardName + "_" +
                districtName + "_" +
                provinceName + "." +
                registrationType.getValue() + "." +
                numberBookYear + "." +
                numberBook + "." +
                "xlsx";

        Long ts = new Date().getTime();

        ExportHistory newExportHistory = new ExportHistory()
                .setProjectId(projectWard.getProject().getId())
                .setProjectName(projectWard.getProject().getName())
                .setProvinceId(projectWard.getProjectDistrict().getProjectProvince().getId())
                .setProvinceName(projectWard.getProjectDistrict().getProjectProvince().getName())
                .setDistrictId(projectWard.getProjectDistrict().getId())
                .setDistrictName(projectWard.getProjectDistrict().getName())
                .setWardId(projectWard.getId())
                .setWardName(projectWard.getName())
                .setRegistrationType(registrationType.getValue())
                .setPaperSize(paperSize.getValue())
                .setNumberBookYear(numberBookYear)
                .setNumberBook(numberBook)
                .setExcelFolderName(excelFolder)
                .setExcelName(excelName)
                .setTs(ts)
                .setCreatedBy(user)
                .setExportType(EExportType.EXCEL);

        Optional<ExportHistory> exportHistoryOptional = exportHistoryRepository.findByExcelName(excelName);

        if (exportHistoryOptional.isPresent()) {
            ExportHistory oldExportHistory = exportHistoryOptional.get();
            String excelNewName = excelName.replace(".xlsx", "") + "_" + ts + ".xlsx";

            File oldFile = new File(serverRootFolder + "/" + excelFolder + "/" + excelName);
            File newFile = new File(serverRootFolder + "/" + excelFolder + "/" + excelNewName);

            boolean success = oldFile.renameTo(newFile);

            if (success) {
                oldExportHistory.setExcelName(excelNewName);
                exportHistoryRepository.saveAndFlush(oldExportHistory);
            }
            else {
                logger.error("Không thể đặt lại tên file " + excelName);
                return;
            }
        }

        exportExcel(newExportHistory, excelName, projectWard, registrationType, numberBookYear, numberBook);
    }

    public void exportExcel(
            ExportHistory exportHistory,
            String excelName,
            ProjectWard projectWard,
            ERegistrationType registrationType,
            String numberBookYear,
            String numberBook
    ) {
        numberBook = numberBook + "/" + numberBookYear;

        switch (registrationType) {
            case CMC -> {
                List<ParentsChildren> parentsChildrenList = parentsChildrenRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (parentsChildrenList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                exportHistory.setTotalCount(parentsChildrenList.size());
                exportHistoryRepository.saveAndFlush(exportHistory);

                // thực hiện xuất excel sau 10s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(parentsChildrenList, registrationType, excelName, registrationType.getValue(), exportHistory);
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case KS -> {
                List<Birth> birthList = birthRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (birthList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                exportHistory.setTotalCount(birthList.size());
                exportHistoryRepository.saveAndFlush(exportHistory);

                // thực hiện xuất excel sau 10s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(birthList, registrationType, excelName, registrationType.getValue(), exportHistory);
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case KH -> {
                List<Marry> marryList = marryRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (marryList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                exportHistory.setTotalCount(marryList.size());
                exportHistoryRepository.saveAndFlush(exportHistory);

                // thực hiện xuất excel sau 10s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(marryList, registrationType, excelName, registrationType.getValue(), exportHistory);
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case HN -> {
                List<Wedlock> wedlockList = wedlockRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (wedlockList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                exportHistory.setTotalCount(wedlockList.size());
                exportHistoryRepository.saveAndFlush(exportHistory);

                // thực hiện xuất excel sau 10s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(wedlockList, registrationType, excelName, registrationType.getValue(), exportHistory);
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case KT -> {
                List<Death> deathList = deathRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (deathList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                exportHistory.setTotalCount(deathList.size());
                exportHistoryRepository.saveAndFlush(exportHistory);

                // thực hiện xuất excel sau 10s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(deathList, registrationType, excelName, registrationType.getValue(), exportHistory);
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
        }
    }

    @Override
    public void delete(ExportHistory exportHistory) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
