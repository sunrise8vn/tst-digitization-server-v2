package com.tst.services.exporter;

import com.tst.models.entities.*;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.repositories.ExportHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExcelExportService implements IExcelExportService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);

    @Value("${server.root-folder}")
    private String serverRootFolder;

    @Value("${server.excel-folder}")
    private String excelFolder;


    private final ExportHistoryRepository exportHistoryRepository;

    @Override
    public void exportToExcel(
            List<?> list,
            ERegistrationType registrationType,
            String fileName,
            String sheetName,
            ExportHistory exportHistory
    ) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            // mã màu header #0070C0
            byte[] rgbBytes = new byte[] { (byte) 0x00, (byte) 0x70, (byte) 0xC0 };
            XSSFColor customColor = new XSSFColor(rgbBytes, null);

            XSSFCellStyle headerColumnStyle = (XSSFCellStyle) workbook.createCellStyle();
            headerColumnStyle.setFont(headerFont);
            headerColumnStyle.setFillForegroundColor(customColor);
            headerColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Tạo tiêu đề cột
            Row headerRow = sheet.createRow(0);
            String[] columns = writeHeaderLine(registrationType);

            for (int i = 0; i < columns.length; i++) {
                Cell headerColumn = headerRow.createCell(i);
                headerColumn.setCellValue(columns[i]);
                headerColumn.setCellStyle(headerColumnStyle);
            }

            // Thêm dữ liệu vào sheet
            switch (registrationType) {
                case CMC -> {
                    List<ParentsChildren> parentsChildrenList = list.stream()
                            .map(item -> (ParentsChildren) item)
                            .toList();

                    writeParentsChildrenDataLines(sheet, parentsChildrenList);
                }
                case KS -> {
                    List<Birth> birthList = list.stream()
                            .map(item -> (Birth) item)
                            .toList();

                    writeBirthDataLines(sheet, birthList);
                }
                case KH -> {
                    List<Marry> marryList = list.stream()
                            .map(item -> (Marry) item)
                            .toList();

                    writeMarryDataLines(sheet, marryList);
                }
                case HN -> {
                    List<Wedlock> wedlockList = list.stream()
                            .map(item -> (Wedlock) item)
                            .toList();

                    writeWedlockDataLines(sheet, wedlockList);
                }
                case KT -> {
                    List<Death> deathList = list.stream()
                            .map(item -> (Death) item)
                            .toList();

                    writeDeathDataLines(sheet, deathList);
                }
            }

            Path rootPath = Paths.get(serverRootFolder);

            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            Path destinationPath = Paths.get(rootPath + "/" + excelFolder);

            // Ghi workbook vào tệp
            File file = new File(destinationPath + "/" + fileName);

            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException("Không tạo được thư mục gốc");
                }
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            // Lấy kích thước của tệp KB
            long fileSize = file.length() / 1024;

            exportHistory.setExcelSize(fileSize);
            exportHistory.setStatus(EExportStatus.SUCCESS);
            exportHistoryRepository.save(exportHistory);

            System.out.println("File exported successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            exportHistory.setStatus(EExportStatus.FAILED);
            exportHistoryRepository.save(exportHistory);

            logger.error(e.getMessage());
        }
    }

    private String[] writeHeaderLine(ERegistrationType registrationType) {
        String[] columns = new String[] {};

        switch (registrationType) {
            case CMC ->
                // 50 cols
                columns = new String[] {
                    "so", "quyenSo", "trangSo", "quyetDinhSo", "ngayDangKy", "loaiDangKy", "loaiXacNhan", "noiDangKy",
                    "nguoiKy", "chucVuNguoiKy", "nguoiThucHien", "ghiChu", "cmHoTen", "cmNgaySinh", "cmDanToc",
                    "cmQuocTich", "cmQuocTichKhac", "cmQueQuan", "cmLoaiCuTru", "cmNoiCuTru", "cmLoaiGiayToTuyThan",
                    "cmGiayToKhac", "cmSoGiayToTuyThan", "cmNgayCapGiayToTuyThan", "cmNoiCapGiayToTuyThan", "ncHoTen",
                    "ncNgaySinh", "ncDanToc", "ncQuocTich", "ncQuocTichKhac", "ncQueQuan", "ncLoaiCuTru", "ncNoiCuTru",
                    "ncLoaiGiayToTuyThan", "ncGiayToKhac", "ncSoGiayToTuyThan", "ncNgayCapGiayToTuyThan",
                    "ncNoiCapGiayToTuyThan", "nycHoTen", "nycQHNguoiDuocNhan", "nycQHNguoiNhan", "nycLoaiGiayToTuyThan",
                    "nycGiayToKhac", "nycSoGiayToTuyThan", "nycNgayCapGiayToTuyThan", "nycNoiCapGiayToTuyThan",
                    "soDangKyNuocNgoai", "ngayDangKyNuocNgoai", "cqNuocNgoaiDaDangKy", "qgNuocNgoaiDaDangKy"
                };
            case KS ->
                // 60 cols
                columns = new String[] {
                    "so", "quyenSo", "trangSo", "ngayDangKy", "loaiDangKy", "noiDangKy",
                    "nguoiKy", "chucVuNguoiKy", "nguoiThucHien", "ghiChu", "nksHoTen", "nksGioiTinh",
                    "nksNgaySinh", "nksNgaySinhBangChu", "nksNoiSinh", "nksNoiSinhDVHC", "nksQueQuan", "nksDanToc",
                    "nksQuocTich", "nksQuocTichKhac", "nksLoaiKhaiSinh", "nksMatTich", "nksMatTichNgayGhiChuTuyenBo",
                    "nksMatTichCanCuTuyenBo", "nksMatTichNgayGhiChuHuyTuyenBo", "nksMatTichCanCuHuyTuyenBo",
                    "nksHanCheNangLucHanhVi", "nksHanCheNangLucHanhViNgayGhiChuTuyenBo", "nksHanCheNangLucHanhViCanCuTuyenBo",
                    "nksHanCheNangLucHanhViNgayGhiChuHuyTuyenBo", "nksHanCheNangLucHanhViNgayCanCuHuyTuyenBo",
                    "meHoTen", "meNgaySinh", "meDanToc", "meQuocTich", "meQuocTichKhac", "meLoaiCuTru",
                    "meNoiCuTru", "meLoaiGiayToTuyThan", "meSoGiayToTuyThan", "chaHoTen", "chaNgaySinh",
                    "chaDanToc", "chaQuocTich", "chaQuocTichKhac", "chaLoaiCuTru", "chaNoiCuTru",
                    "chaLoaiGiayToTuyThan", "chaSoGiayToTuyThan", "nycHoTen", "nycQuanHe", "nycLoaiGiayToTuyThan",
                    "nycGiayToKhac", "nycSoGiayToTuyThan", "nycNgayCapGiayToTuyThan", "nycNoiCapGiayToTuyThan",
                    "soDangKyNuocNgoai", "ngayDangKyNuocNgoai", "cqNuocNgoaiDaDangKy", "qgNuocNgoaiDaDangKy"
            };
            case KH ->
                // 44 cols
                columns = new String[] {
                    "so", "quyenSo", "trangSo", "ngayDangKy", "loaiDangKy", "noiDangKy",
                    "nguoiKy", "chucVuNguoiKy", "ngayXacLapQuanHeHonNhan", "nguoiThucHien", "ghiChu", "tinhTrangKetHon",
                    "huyKetHonNgayGhiChu", "huyKetHonCanCu", "congNhanKetHonNgayGhiChu", "congNhanKetHonCanCu",
                    "chongHoTen", "chongNgaySinh", "chongDanToc", "chongQuocTich", "chongQuocTichKhac",
                    "chongLoaiCuTru", "chongNoiCuTru", "chongLoaiGiayToTuyThan", "chongGiayToKhac", "chongSoGiayToTuyThan",
                    "chongNgayCapGiayToTuyThan", "chongNoiCapGiayToTuyThan", "voHoTen", "voNgaySinh", "voDanToc",
                    "voQuocTich", "voQuocTichKhac", "voLoaiCuTru", "voNoiCuTru", "voLoaiGiayToTuyThan", "voGiayToKhac",
                    "voSoGiayToTuyThan", "voNgayCapGiayToTuyThan", "voNoiCapGiayToTuyThan",
                    "soDangKyNuocNgoai", "ngayDangKyNuocNgoai", "cqNuocNgoaiDaDangKy", "qgNuocNgoaiDaDangKy"
            };
            case HN ->
                // 35 cols
                columns = new String[] {
                    "so", "quyenSo", "trangSo", "ngayDangKy", "noiCap", "nguoiKy", "chucVuNguoiKy", "nguoiThucHien", "ghiChu",
                    "nxnHoTen", "nxnGioiTinh", "nxnNgaySinh", "nxnDanToc", "nxnQuocTich", "nxnQuocTichKhac",
                    "nxnLoaiCuTru", "nxnNoiCuTru", "nxnLoaiGiayToTuyThan", "nxnGiayToKhac", "nxnSoGiayToTuyThan",
                    "nxnNgayCapGiayToTuyThan", "nxnNoiCapGiayToTuyThan", "nxnThoiGianCuTruTai", "nxnThoiGianCuTruTu",
                    "nxnThoiGianCuTruDen", "nxnTinhTrangHonNhan", "nxnLoaiMucDichSuDung", "nxnMucDichSuDung",
                    "nycHoTen", "nycQuanHe", "nycLoaiGiayToTuyThan", "nycGiayToKhac", "nycSoGiayToTuyThan",
                    "nycNgayCapGiayToTuyThan", "nycNoiCapGiayToTuyThan"
            };
            case KT ->
                // 48 cols
                columns = new String[] {
                    "so", "quyenSo", "trangSo", "ngayDangKy", "loaiDangKy", "noiDangKy", "nguoiKy", "chucVuNguoiKy",
                    "nguoiThucHien", "ghiChu", "nktHoTen", "nktGioiTinh", "nktNgaySinh", "nktDanToc", "nktQuocTich",
                    "nktQuocTichKhac", "nktLoaiCuTru", "nktNoiCuTru", "nktLoaiGiayToTuyThan", "nktGiayToKhac",
                    "nktSoGiayToTuyThan", "nktNgayCapGiayToTuyThan", "nktNoiCapGiayToTuyThan", "nktNgayChet",
                    "nktNgayChetGhiBangChu", "nktGioPhutChet", "nktNoiChet", "nktNguyenNhanChet",
                    "nktTinhTrangTuyenBoViecChet", "nktNgayGhiChuTuyenBoViecChet", "nktCanCuTuyenBoViecChet",
                    "nktNgayGhiChuHuyTuyenBoViecChet", "nktCanCuHuyTuyenBoViecChet", "gbtLoai", "gbtSo", "gbtNgay",
                    "gbtCoQuanCap", "nycHoTen", "nycQuanHe", "nycLoaiGiayToTuyThan", "nycGiayToKhac",
                    "nycSoGiayToTuyThan", "nycNgayCapGiayToTuyThan", "nycNoiCapGiayToTuyThan",
                    "soDangKyNuocNgoai", "ngayDangKyNuocNgoai", "cqNuocNgoaiDaDangKy", "qgNuocNgoaiDaDangKy"
            };
        }

        return columns;
    }

    private void writeParentsChildrenDataLines(Sheet sheet, List<ParentsChildren> parentsChildrenList) {
        int rowNum = 1;
        for (ParentsChildren item : parentsChildrenList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNumber());
            row.createCell(1).setCellValue(item.getNumberBook());
            row.createCell(2).setCellValue(item.getNumberPage());
            row.createCell(3).setCellValue(item.getDecisionNo());
            row.createCell(4).setCellValue(item.getRegistrationDate());
            row.createCell(5).setCellValue(item.getRegistrationType());
            row.createCell(6).setCellValue(item.getConfirmationType());
            row.createCell(7).setCellValue(item.getRegistrationPlace());
            row.createCell(8).setCellValue(item.getSigner());
            row.createCell(9).setCellValue(item.getSignerPosition());
            row.createCell(10).setCellValue(item.getImplementer());
            row.createCell(11).setCellValue(item.getNote());
            row.createCell(12).setCellValue(item.getParentFullName());
            row.createCell(13).setCellValue(item.getParentBirthday());
            row.createCell(14).setCellValue(item.getParentNation());
            row.createCell(15).setCellValue(item.getParentNationality());
            row.createCell(16).setCellValue(item.getParentOtherNationality());
            row.createCell(17).setCellValue(item.getParentHomeTown());
            row.createCell(18).setCellValue(item.getParentResidenceType());
            row.createCell(19).setCellValue(item.getParentResidence());
            row.createCell(20).setCellValue(item.getParentIdentificationType());
            row.createCell(21).setCellValue(item.getParentOtherDocument());
            row.createCell(22).setCellValue(item.getParentIdentificationNumber());
            row.createCell(23).setCellValue(item.getParentIdentificationIssuanceDate());
            row.createCell(24).setCellValue(item.getParentIdentificationIssuancePlace());
            row.createCell(25).setCellValue(item.getChildFullName());
            row.createCell(26).setCellValue(item.getChildBirthday());
            row.createCell(27).setCellValue(item.getChildNation());
            row.createCell(28).setCellValue(item.getChildNationality());
            row.createCell(29).setCellValue(item.getChildOtherNationality());
            row.createCell(30).setCellValue(item.getChildHomeTown());
            row.createCell(31).setCellValue(item.getChildResidenceType());
            row.createCell(32).setCellValue(item.getChildResidence());
            row.createCell(33).setCellValue(item.getChildIdentificationType());
            row.createCell(34).setCellValue(item.getChildOtherDocument());
            row.createCell(35).setCellValue(item.getChildIdentificationNumber());
            row.createCell(36).setCellValue(item.getChildIdentificationIssuanceDate());
            row.createCell(37).setCellValue(item.getChildIdentificationIssuancePlace());
            row.createCell(38).setCellValue(item.getPetitionerFullName());
            row.createCell(39).setCellValue(item.getPetitionerRecipientRelationship());
            row.createCell(40).setCellValue(item.getPetitionerReceiverRelationship());
            row.createCell(41).setCellValue(item.getPetitionerIdentificationType());
            row.createCell(42).setCellValue(item.getPetitionerOtherDocument());
            row.createCell(43).setCellValue(item.getPetitionerIdentificationNumber());
            row.createCell(44).setCellValue(item.getPetitionerIdentificationIssuanceDate());
            row.createCell(45).setCellValue(item.getPetitionerIdentificationIssuancePlace());
            row.createCell(46).setCellValue(item.getForeignRegistrationNumber());
            row.createCell(47).setCellValue(item.getForeignRegistrationDate());
            row.createCell(48).setCellValue(item.getRegisteredForeignOrganization());
            row.createCell(49).setCellValue(item.getRegisteredForeignCountry());
        }
    }

    private void writeBirthDataLines(Sheet sheet, List<Birth> birthList) {
        int rowNum = 1;
        for (Birth item : birthList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNumber());
            row.createCell(1).setCellValue(item.getNumberBook());
            row.createCell(2).setCellValue(item.getNumberPage());
            row.createCell(3).setCellValue(item.getRegistrationDate());
            row.createCell(4).setCellValue(item.getRegistrationType());
            row.createCell(5).setCellValue(item.getRegistrationPlace());
            row.createCell(6).setCellValue(item.getSigner());
            row.createCell(7).setCellValue(item.getSignerPosition());
            row.createCell(8).setCellValue(item.getImplementer());
            row.createCell(9).setCellValue(item.getNote());
            row.createCell(10).setCellValue(item.getBirtherFullName());
            row.createCell(11).setCellValue(item.getBirtherGender());
            row.createCell(12).setCellValue(item.getBirtherBirthday());
            row.createCell(13).setCellValue(item.getBirtherBirthdayWord());
            row.createCell(14).setCellValue(item.getBirtherBirthPlace());
            row.createCell(15).setCellValue(item.getBirtherBirthPlaceAdministrativeUnit());
            row.createCell(16).setCellValue(item.getBirtherHomeTown());
            row.createCell(17).setCellValue(item.getBirtherNation());
            row.createCell(18).setCellValue(item.getBirtherNationality());
            row.createCell(19).setCellValue(item.getBirtherOtherNationality());
            row.createCell(20).setCellValue(item.getBirtherBirthCertificateType());
            row.createCell(21).setCellValue(item.getBirtherMissing());
            row.createCell(22).setCellValue(item.getBirtherMissingStatementNoteDate());
            row.createCell(23).setCellValue(item.getBirtherMissingStatementBase());
            row.createCell(24).setCellValue(item.getBirtherMissingCancelStatementNoteDate());
            row.createCell(25).setCellValue(item.getBirtherMissingCancelStatementBase());
            row.createCell(26).setCellValue(item.getBirtherLimitedBehavioralCapacity());
            row.createCell(27).setCellValue(item.getBirtherLimitedBehavioralCapacityStatementNoteDate());
            row.createCell(28).setCellValue(item.getBirtherLimitedBehavioralCapacityStatementBase());
            row.createCell(29).setCellValue(item.getBirtherLimitedBehavioralCapacityCancelStatementNoteDate());
            row.createCell(30).setCellValue(item.getBirtherLimitedBehavioralCapacityCancelStatementBaseDate());
            row.createCell(31).setCellValue(item.getMomFullName());
            row.createCell(32).setCellValue(item.getMomBirthday());
            row.createCell(33).setCellValue(item.getMomNation());
            row.createCell(34).setCellValue(item.getMomNationality());
            row.createCell(35).setCellValue(item.getMomOtherNationality());
            row.createCell(36).setCellValue(item.getMomResidenceType());
            row.createCell(37).setCellValue(item.getMomResidence());
            row.createCell(38).setCellValue(item.getMomIdentificationType());
            row.createCell(39).setCellValue(item.getMomIdentificationNumber());
            row.createCell(40).setCellValue(item.getDadFullName());
            row.createCell(41).setCellValue(item.getDadBirthday());
            row.createCell(42).setCellValue(item.getDadNation());
            row.createCell(43).setCellValue(item.getDadNationality());
            row.createCell(44).setCellValue(item.getDadOtherNationality());
            row.createCell(45).setCellValue(item.getDadResidenceType());
            row.createCell(46).setCellValue(item.getDadResidence());
            row.createCell(47).setCellValue(item.getDadIdentificationType());
            row.createCell(48).setCellValue(item.getDadIdentificationNumber());
            row.createCell(49).setCellValue(item.getPetitionerFullName());
            row.createCell(50).setCellValue(item.getPetitionerRelationship());
            row.createCell(51).setCellValue(item.getPetitionerIdentificationType());
            row.createCell(52).setCellValue(item.getPetitionerOtherDocument());
            row.createCell(53).setCellValue(item.getPetitionerIdentificationNumber());
            row.createCell(54).setCellValue(item.getPetitionerIdentificationIssuanceDate());
            row.createCell(55).setCellValue(item.getPetitionerIdentificationIssuancePlace());
            row.createCell(56).setCellValue(item.getForeignRegistrationNumber());
            row.createCell(57).setCellValue(item.getForeignRegistrationDate());
            row.createCell(58).setCellValue(item.getRegisteredForeignOrganization());
            row.createCell(59).setCellValue(item.getRegisteredForeignCountry());
        }
    }

    private void writeMarryDataLines(Sheet sheet, List<Marry> marryList) {
        int rowNum = 1;
        for (Marry item : marryList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNumber());
            row.createCell(1).setCellValue(item.getNumberBook());
            row.createCell(2).setCellValue(item.getNumberPage());
            row.createCell(3).setCellValue(item.getRegistrationDate());
            row.createCell(4).setCellValue(item.getRegistrationType());
            row.createCell(5).setCellValue(item.getRegistrationPlace());
            row.createCell(6).setCellValue(item.getSigner());
            row.createCell(7).setCellValue(item.getSignerPosition());
            row.createCell(8).setCellValue(item.getMaritalRelationshipEstablishmentDate());
            row.createCell(9).setCellValue(item.getImplementer());
            row.createCell(10).setCellValue(item.getNote());
            row.createCell(11).setCellValue(item.getMaritalStatus());
            row.createCell(12).setCellValue(item.getCancelMarryNoteDate());
            row.createCell(13).setCellValue(item.getCancelMarryBase());
            row.createCell(14).setCellValue(item.getMarriageRecognitionNoteDate());
            row.createCell(15).setCellValue(item.getMarriageRecognitionBase());
            row.createCell(16).setCellValue(item.getHusbandFullName());
            row.createCell(17).setCellValue(item.getHusbandBirthday());
            row.createCell(18).setCellValue(item.getHusbandNation());
            row.createCell(19).setCellValue(item.getHusbandNationality());
            row.createCell(20).setCellValue(item.getHusbandOtherNationality());
            row.createCell(21).setCellValue(item.getHusbandResidenceType());
            row.createCell(22).setCellValue(item.getHusbandResidence());
            row.createCell(23).setCellValue(item.getHusbandIdentificationType());
            row.createCell(24).setCellValue(item.getHusbandOtherDocument());
            row.createCell(25).setCellValue(item.getHusbandIdentificationNumber());
            row.createCell(26).setCellValue(item.getHusbandIdentificationIssuanceDate());
            row.createCell(27).setCellValue(item.getHusbandIdentificationIssuancePlace());
            row.createCell(28).setCellValue(item.getWifeFullName());
            row.createCell(29).setCellValue(item.getWifeBirthday());
            row.createCell(30).setCellValue(item.getWifeNation());
            row.createCell(31).setCellValue(item.getWifeNationality());
            row.createCell(32).setCellValue(item.getWifeOtherNationality());
            row.createCell(33).setCellValue(item.getWifeResidenceType());
            row.createCell(34).setCellValue(item.getWifeResidence());
            row.createCell(35).setCellValue(item.getWifeIdentificationType());
            row.createCell(36).setCellValue(item.getWifeOtherDocument());
            row.createCell(37).setCellValue(item.getWifeIdentificationNumber());
            row.createCell(38).setCellValue(item.getWifeIdentificationIssuanceDate());
            row.createCell(39).setCellValue(item.getWifeIdentificationIssuancePlace());
            row.createCell(40).setCellValue(item.getForeignRegistrationNumber());
            row.createCell(41).setCellValue(item.getForeignRegistrationDate());
            row.createCell(42).setCellValue(item.getRegisteredForeignOrganization());
            row.createCell(43).setCellValue(item.getRegisteredForeignCountry());
        }
    }

    private void writeWedlockDataLines(Sheet sheet, List<Wedlock> wedlockList) {
        int rowNum = 1;
        for (Wedlock item : wedlockList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNumber());
            row.createCell(1).setCellValue(item.getNumberBook());
            row.createCell(2).setCellValue(item.getNumberPage());
            row.createCell(3).setCellValue(item.getRegistrationDate());
            row.createCell(4).setCellValue(item.getIssuancePlace());
            row.createCell(5).setCellValue(item.getSigner());
            row.createCell(6).setCellValue(item.getSignerPosition());
            row.createCell(7).setCellValue(item.getImplementer());
            row.createCell(8).setCellValue(item.getNote());
            row.createCell(9).setCellValue(item.getConfirmerFullName());
            row.createCell(10).setCellValue(item.getConfirmerGender());
            row.createCell(11).setCellValue(item.getConfirmerBirthday());
            row.createCell(12).setCellValue(item.getConfirmerNation());
            row.createCell(13).setCellValue(item.getConfirmerNationality());
            row.createCell(14).setCellValue(item.getConfirmerOtherNationality());
            row.createCell(15).setCellValue(item.getConfirmerResidenceType());
            row.createCell(16).setCellValue(item.getConfirmerResidence());
            row.createCell(17).setCellValue(item.getConfirmerIdentificationType());
            row.createCell(18).setCellValue(item.getConfirmerOtherDocument());
            row.createCell(19).setCellValue(item.getConfirmerIdentificationNumber());
            row.createCell(20).setCellValue(item.getConfirmerIdentificationIssuanceDate());
            row.createCell(21).setCellValue(item.getConfirmerIdentificationIssuancePlace());
            row.createCell(22).setCellValue(item.getConfirmerPeriodResidenceAt());
            row.createCell(23).setCellValue(item.getConfirmerPeriodResidenceFrom());
            row.createCell(24).setCellValue(item.getConfirmerPeriodResidenceTo());
            row.createCell(25).setCellValue(item.getConfirmerMaritalStatus());
            row.createCell(26).setCellValue(item.getConfirmerIntendedUseType());
            row.createCell(27).setCellValue(item.getConfirmerIntendedUse());
            row.createCell(28).setCellValue(item.getPetitionerFullName());
            row.createCell(29).setCellValue(item.getPetitionerRelationship());
            row.createCell(30).setCellValue(item.getPetitionerIdentificationType());
            row.createCell(31).setCellValue(item.getPetitionerOtherDocument());
            row.createCell(32).setCellValue(item.getPetitionerIdentificationNumber());
            row.createCell(33).setCellValue(item.getPetitionerIdentificationIssuanceDate());
            row.createCell(34).setCellValue(item.getPetitionerIdentificationIssuancePlace());
        }
    }

    private void writeDeathDataLines(Sheet sheet, List<Death> deathList) {
        int rowNum = 1;
        for (Death item : deathList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNumber());
            row.createCell(1).setCellValue(item.getNumberBook());
            row.createCell(2).setCellValue(item.getNumberPage());
            row.createCell(3).setCellValue(item.getRegistrationDate());
            row.createCell(4).setCellValue(item.getRegistrationType());
            row.createCell(5).setCellValue(item.getRegistrationPlace());
            row.createCell(6).setCellValue(item.getSigner());
            row.createCell(7).setCellValue(item.getSignerPosition());
            row.createCell(8).setCellValue(item.getImplementer());
            row.createCell(9).setCellValue(item.getNote());
            row.createCell(10).setCellValue(item.getDeadManFullName());
            row.createCell(11).setCellValue(item.getDeadManGender());
            row.createCell(12).setCellValue(item.getDeadManBirthday());
            row.createCell(13).setCellValue(item.getDeadManNation());
            row.createCell(14).setCellValue(item.getDeadManNationality());
            row.createCell(15).setCellValue(item.getDeadManOtherNationality());
            row.createCell(16).setCellValue(item.getDeadManResidenceType());
            row.createCell(17).setCellValue(item.getDeadManResidence());
            row.createCell(18).setCellValue(item.getDeadManIdentificationType());
            row.createCell(19).setCellValue(item.getDeadManOtherDocument());
            row.createCell(20).setCellValue(item.getDeadManIdentificationNumber());
            row.createCell(21).setCellValue(item.getDeadManIdentificationIssuanceDate());
            row.createCell(22).setCellValue(item.getDeadManIdentificationIssuancePlace());
            row.createCell(23).setCellValue(item.getDeadManDeadDate());
            row.createCell(24).setCellValue(item.getDeadManDeadDateWord());
            row.createCell(25).setCellValue(item.getDeadManDeadTime());
            row.createCell(26).setCellValue(item.getDeadManDeadPlace());
            row.createCell(27).setCellValue(item.getDeadManDeadReason());
            row.createCell(28).setCellValue(item.getDeadManDeathStatementStatus());
            row.createCell(29).setCellValue(item.getDeadManDeathStatementNoteDate());
            row.createCell(30).setCellValue(item.getDeadManDeathStatementBase());
            row.createCell(31).setCellValue(item.getDeadManDeathCancelStatementNoteDate());
            row.createCell(32).setCellValue(item.getDeadManDeathCancelStatementBase());
            row.createCell(33).setCellValue(item.getDeathNoticeType());
            row.createCell(34).setCellValue(item.getDeathNoticeNumber());
            row.createCell(35).setCellValue(item.getDeathNoticeDate());
            row.createCell(36).setCellValue(item.getDeathNoticeIssuancePlace());
            row.createCell(37).setCellValue(item.getPetitionerFullName());
            row.createCell(38).setCellValue(item.getPetitionerRelationship());
            row.createCell(39).setCellValue(item.getPetitionerIdentificationType());
            row.createCell(40).setCellValue(item.getPetitionerOtherDocument());
            row.createCell(41).setCellValue(item.getPetitionerIdentificationNumber());
            row.createCell(42).setCellValue(item.getPetitionerIdentificationIssuanceDate());
            row.createCell(43).setCellValue(item.getPetitionerIdentificationIssuancePlace());
            row.createCell(44).setCellValue(item.getForeignRegistrationNumber());
            row.createCell(45).setCellValue(item.getForeignRegistrationDate());
            row.createCell(46).setCellValue(item.getRegisteredForeignOrganization());
            row.createCell(47).setCellValue(item.getRegisteredForeignCountry());
        }
    }
}
