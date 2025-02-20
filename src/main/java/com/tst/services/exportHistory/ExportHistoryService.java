package com.tst.services.exportHistory;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.*;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.EExportType;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.*;
import com.tst.services.exporter.IExcelExportService;
import com.tst.services.exporter.IZipPdfExportService;
import com.tst.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    @Value("${server.zip-folder}")
    private String zipFolder;

    private final ExportHistoryRepository exportHistoryRepository;
    private final ParentsChildrenRepository parentsChildrenRepository;
    private final BirthRepository birthRepository;
    private final MarryRepository marryRepository;
    private final WedlockRepository wedlockRepository;
    private final DeathRepository deathRepository;

    private final IExcelExportService excelExportService;
    private final IZipPdfExportService zipPdfExportService;

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

            if (oldFile.exists()) {
                boolean renameSuccess = oldFile.renameTo(newFile);

                if (!renameSuccess) {
                    logger.error("Không thể đặt lại tên file " + excelName);
                    throw new DataInputException("Không thể đặt lại tên tệp cũ để xuất excel");
                }
            }

            oldExportHistory.setZipName(excelNewName);
            exportHistoryRepository.saveAndFlush(oldExportHistory);
        }

        exportExcel(
                newExportHistory,
                excelName,
                projectWard,
                registrationType,
                numberBookYear,
                numberBook
        );
    }

    public void exportExcel(
            ExportHistory newExportHistory,
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

                newExportHistory.setTotalCount(parentsChildrenList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            parentsChildrenList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.EXCEL
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case KS -> {
                List<Birth> birthList = birthRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (birthList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                newExportHistory.setTotalCount(birthList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            birthList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.EXCEL
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case KH -> {
                List<Marry> marryList = marryRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (marryList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                newExportHistory.setTotalCount(marryList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            marryList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.EXCEL
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case HN -> {
                List<Wedlock> wedlockList = wedlockRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (wedlockList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                newExportHistory.setTotalCount(wedlockList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            wedlockList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.EXCEL
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case KT -> {
                List<Death> deathList = deathRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (deathList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel");
                }

                newExportHistory.setTotalCount(deathList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            deathList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.EXCEL
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
        }
    }

    @Override
    public void asyncExportZipPdf(
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

        String zipName = wardName + "_" +
                districtName + "_" +
                provinceName + "." +
                registrationType.getValue() + "." +
                numberBookYear + "." +
                numberBook + "." +
                "zip";

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
                .setZipFolderName(zipFolder)
                .setZipName(zipName)
                .setTs(ts)
                .setCreatedBy(user)
                .setExportType(EExportType.ZIP_PDF);

        Optional<ExportHistory> exportHistoryOptional = exportHistoryRepository.findByZipName(zipName);

        if (exportHistoryOptional.isPresent()) {
            ExportHistory oldExportHistory = exportHistoryOptional.get();
            String zipNewName = zipName.replace(".zip", "") + "_" + ts + ".zip";

            File oldFile = new File(serverRootFolder + "/" + zipFolder + "/" + zipName);
            File newFile = new File(serverRootFolder + "/" + zipFolder + "/" + zipNewName);

            if (oldFile.exists()) {
                boolean success = oldFile.renameTo(newFile);

                if (!success) {
                    logger.error("Không thể đặt lại tên file " + zipName);
                    throw new DataInputException("Không thể đặt lại tên tệp cũ để xuất zip pdf");
                }
            }

            oldExportHistory.setZipName(zipNewName);
            exportHistoryRepository.saveAndFlush(oldExportHistory);
        }

        exportZipPdf(newExportHistory, zipName, projectWard, registrationType, numberBookYear, numberBook);
    }

    public void exportZipPdf(
            ExportHistory newExportHistory,
            String zipName,
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
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất zip pdf");
                }

                newExportHistory.setTotalCount(parentsChildrenList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                String pdfFolder = parentsChildrenList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 5s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            parentsChildrenList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case KS -> {
                List<Birth> birthList = birthRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (birthList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất zip pdf");
                }

                newExportHistory.setTotalCount(birthList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                String pdfFolder = birthList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 5s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            birthList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case KH -> {
                List<Marry> marryList = marryRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (marryList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất zip pdf");
                }

                newExportHistory.setTotalCount(marryList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                String pdfFolder = marryList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 5s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            marryList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case HN -> {
                List<Wedlock> wedlockList = wedlockRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (wedlockList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất zip pdf");
                }

                newExportHistory.setTotalCount(wedlockList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                String pdfFolder = wedlockList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 5s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            wedlockList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
            case KT -> {
                List<Death> deathList = deathRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (deathList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất zip pdf");
                }

                newExportHistory.setTotalCount(deathList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                String pdfFolder = deathList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 5s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            deathList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
            }
        }
    }

    @Override
    public void asyncExportExcelAndZipPdf(
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

        String fileName = wardName + "_" +
                districtName + "_" +
                provinceName + "." +
                registrationType.getValue() + "." +
                numberBookYear + "." +
                numberBook;

        String excelName = fileName + ".xlsx";
        String zipName = fileName + ".zip";

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
                .setZipFolderName(zipFolder)
                .setZipName(zipName)
                .setTs(ts)
                .setCreatedBy(user)
                .setExportType(EExportType.BOTH);

        Optional<ExportHistory> excelExportHistoryOptional = exportHistoryRepository.findByExcelName(excelName);

        if (excelExportHistoryOptional.isPresent()) {
            ExportHistory oldExportHistory = excelExportHistoryOptional.get();
            String excelNewName = excelName.replace(".xlsx", "") + "_" + ts + ".xlsx";

            File oldFile = new File(serverRootFolder + "/" + excelFolder + "/" + excelName);
            File newFile = new File(serverRootFolder + "/" + excelFolder + "/" + excelNewName);

            if (oldFile.exists()) {
                boolean success = oldFile.renameTo(newFile);

                if (!success) {
                    logger.error("Không thể đặt lại tên file " + excelName);
                    throw new DataInputException("Không thể đặt lại tên tệp cũ để xuất excel");
                }
            }

            oldExportHistory.setExcelName(excelNewName);
            exportHistoryRepository.saveAndFlush(oldExportHistory);
        }

        Optional<ExportHistory> zipExportHistoryOptional = exportHistoryRepository.findByZipName(zipName);

        if (zipExportHistoryOptional.isPresent()) {
            ExportHistory oldExportHistory = zipExportHistoryOptional.get();
            String zipNewName = zipName.replace(".zip", "") + "_" + ts + ".zip";

            File oldFile = new File(serverRootFolder + "/" + zipFolder + "/" + zipName);
            File newFile = new File(serverRootFolder + "/" + zipFolder + "/" + zipNewName);

            if (oldFile.exists()) {
                boolean success = oldFile.renameTo(newFile);

                if (!success) {
                    logger.error("Không thể đặt lại tên file " + zipName);
                    throw new DataInputException("Không thể đặt lại tên tệp cũ để xuất zip pdf");
                }
            }

            oldExportHistory.setZipName(zipNewName);
            exportHistoryRepository.saveAndFlush(oldExportHistory);
        }

        exportExcelAndZipPdf(
                newExportHistory,
                excelName,
                zipName,
                projectWard,
                registrationType,
                numberBookYear,
                numberBook
        );
    }

    public void exportExcelAndZipPdf(
            ExportHistory newExportHistory,
            String excelName,
            String zipName,
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
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel và pdf");
                }

                newExportHistory.setTotalCount(parentsChildrenList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            parentsChildrenList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.BOTH
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

                String pdfFolder = parentsChildrenList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 10s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            parentsChildrenList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case KS -> {
                List<Birth> birthList = birthRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (birthList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel và pdf");
                }

                newExportHistory.setTotalCount(birthList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            birthList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.BOTH
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

                String pdfFolder = birthList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 10s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            birthList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case KH -> {
                List<Marry> marryList = marryRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (marryList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel và pdf");
                }

                newExportHistory.setTotalCount(marryList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            marryList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.BOTH
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

                String pdfFolder = marryList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 10s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            marryList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case HN -> {
                List<Wedlock> wedlockList = wedlockRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (wedlockList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel và pdf");
                }

                newExportHistory.setTotalCount(wedlockList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            wedlockList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.BOTH
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

                String pdfFolder = wedlockList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 10s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            wedlockList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
                }, CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));
            }
            case KT -> {
                List<Death> deathList = deathRepository.findAllByProjectWardAndNumberBook(
                        projectWard,
                        numberBook
                );

                if (deathList.isEmpty()) {
                    throw new DataInputException("Tệp dữ liệu này không có biểu mẫu tương ứng để xuất excel và pdf");
                }

                newExportHistory.setTotalCount(deathList.size());
                exportHistoryRepository.saveAndFlush(newExportHistory);

                // thực hiện xuất excel sau 5s
                CompletableFuture.runAsync(() -> {
                    excelExportService.exportToExcel(
                            deathList,
                            registrationType,
                            excelName,
                            registrationType.getValue(),
                            newExportHistory,
                            EExportType.BOTH
                    );
                }, CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));

                String pdfFolder = deathList.get(0).getProjectNumberBookFile().getFolderPath();

                // thực hiện xuất zip pdf sau 10s
                CompletableFuture.runAsync(() -> {
                    zipPdfExportService.exportToZipPdf(
                            deathList,
                            registrationType,
                            zipName,
                            registrationType.getValue(),
                            newExportHistory,
                            pdfFolder
                    );
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
