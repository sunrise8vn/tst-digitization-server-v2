package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.*;
import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.exporter.ExportHistoryResponse;
import com.tst.services.exportHistory.IExportHistoryService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("${api.prefix}/export-data")
@RequiredArgsConstructor
@Validated
public class ExportDataAPI {
    private final IUserService userService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectWardService projectWardService;
    private final IExportHistoryService exportHistoryService;

    private final ModelMapper modelMapper;

    private final AppUtils appUtils;


    @GetMapping("/get-all-by-province/{provinceId}")
    public ResponseEntity<ResponseObject> getAllByProvince(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tỉnh / thành phố phải là một số") String provinceId
    ) {
        ProjectProvince projectProvince = projectProvinceService.findById(
                Long.parseLong(provinceId)
        ).orElseThrow(() -> new DataInputException("Tỉnh / Thành phố không tồn tại"));

        List<ExportHistory> exportHistories = exportHistoryService.findAllByProvinceIdOrderByIdDesc(projectProvince.getId());

        List<ExportHistoryResponse> exportHistoryResponses = exportHistories.stream()
                .map(eh -> modelMapper.map(eh, ExportHistoryResponse.class))
                .toList();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trích xuất theo tỉnh / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(exportHistoryResponses)
                .build());
    }

    @GetMapping("/get-all-by-district/{districtId}")
    public ResponseEntity<ResponseObject> getAllByDistrict(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID quận / huyện / thành phố phải là một số") String districtId
    ) {
        ProjectDistrict projectDistrict = projectDistrictService.findById(
                Long.parseLong(districtId)
        ).orElseThrow(() -> new DataInputException("Quận / Huyện / Thành phố không tồn tại"));

        List<ExportHistory> exportHistories = exportHistoryService.findAllByDistrictIdOrderByIdDesc(projectDistrict.getId());

        List<ExportHistoryResponse> exportHistoryResponses = exportHistories.stream()
                .map(eh -> modelMapper.map(eh, ExportHistoryResponse.class))
                .toList();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trích xuất theo quận / huyện / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(exportHistoryResponses)
                .build());
    }

    @GetMapping("/get-all-by-ward/{wardId}")
    public ResponseEntity<ResponseObject> getAllByWard(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID phường / xã / thị trấn phải là một số") String wardId
    ) {
        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> new DataInputException("Phường / Xã / Thị trấn không tồn tại"));

        List<ExportHistory> exportHistories = exportHistoryService.findAllByWardIdOrderByIdDesc(projectWard.getId());

        List<ExportHistoryResponse> exportHistoryResponses = exportHistories.stream()
                .map(eh -> modelMapper.map(eh, ExportHistoryResponse.class))
                .toList();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trích xuất theo phường / xã / thị trấn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(exportHistoryResponses)
                .build());
    }

    @PostMapping("/excel/{wardId}/{registrationType}/{paperSize}/{numberBook}/{numberBookYear}")
    public CompletableFuture<ResponseEntity<ResponseObject>> exportExcel(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String wardId,
            @PathVariable @NotEmpty(message = "Loại sổ là bắt buộc") String registrationType,
            @PathVariable @NotEmpty(message = "Kích thước sổ là bắt buộc") String paperSize,
            @PathVariable @NotEmpty(message = "Quyển số là bắt buộc") String numberBook,
            @PathVariable @NotEmpty(message = "Năm mở sổ là bắt buộc") String numberBookYear
    ) {
        boolean isValidRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isValidRegistrationType) {
            throw new DataInputException("Loại sổ không tồn tại");
        }

        boolean isValidPaperSize = EPaperSize.checkValue(paperSize.toUpperCase());

        if (!isValidPaperSize) {
            throw new DataInputException("Kích thước sổ không tồn tại");
        }

        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> new DataInputException("Phường / Xã / Thị trấn không tồn tại"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        EPaperSize ePaperSize = EPaperSize.valueOf(paperSize.toUpperCase());

        User user = userService.getAuthenticatedUser();

        Optional<ExportHistory> exportHistoryOptional = exportHistoryService.findByAll(
                projectWard.getProject().getId(),
                projectWard.getId(),
                registrationType,
                paperSize,
                numberBookYear,
                numberBook,
                EExportStatus.PROCESSING
        );

        if (exportHistoryOptional.isPresent()) {
            throw new DataInputException("Tệp dữ liệu trích xuất excel này đang được xử lý");
        }

        exportHistoryService.asyncExportExcel(
                projectWard,
                eRegistrationType,
                ePaperSize,
                numberBookYear,
                numberBook,
                user
        );

        return CompletableFuture.completedFuture(ResponseEntity.ok().body(ResponseObject.builder()
                .message("Trích xuất dữ liệu excel thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build()));
    }

    @PostMapping("/zip-pdf/{wardId}/{registrationType}/{paperSize}/{numberBook}/{numberBookYear}")
    public CompletableFuture<ResponseEntity<ResponseObject>> exportZipPdf(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String wardId,
            @PathVariable @NotEmpty(message = "Loại sổ là bắt buộc") String registrationType,
            @PathVariable @NotEmpty(message = "Kích thước sổ là bắt buộc") String paperSize,
            @PathVariable @NotEmpty(message = "Quyển số là bắt buộc") String numberBook,
            @PathVariable @NotEmpty(message = "Năm mở sổ là bắt buộc") String numberBookYear
    ) {
        boolean isValidRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isValidRegistrationType) {
            throw new DataInputException("Loại sổ không tồn tại");
        }

        boolean isValidPaperSize = EPaperSize.checkValue(paperSize.toUpperCase());

        if (!isValidPaperSize) {
            throw new DataInputException("Kích thước sổ không tồn tại");
        }

        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> new DataInputException("Phường / Xã / Thị trấn không tồn tại"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        EPaperSize ePaperSize = EPaperSize.valueOf(paperSize.toUpperCase());

        User user = userService.getAuthenticatedUser();

        Optional<ExportHistory> exportHistoryOptional = exportHistoryService.findByAll(
                projectWard.getProject().getId(),
                projectWard.getId(),
                registrationType,
                paperSize,
                numberBookYear,
                numberBook,
                EExportStatus.PROCESSING
        );

        if (exportHistoryOptional.isPresent()) {
            throw new DataInputException("Tệp dữ liệu trích xuất excel này đang được xử lý");
        }

        exportHistoryService.asyncExportZipPdf(
                projectWard,
                eRegistrationType,
                ePaperSize,
                numberBookYear,
                numberBook,
                user
        );

        return CompletableFuture.completedFuture(ResponseEntity.ok().body(ResponseObject.builder()
                .message("Trích xuất tệp zip pdf thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build()));
    }

    @PostMapping("/excel-and-zip-pdf/{wardId}/{registrationType}/{paperSize}/{numberBook}/{numberBookYear}")
    public CompletableFuture<ResponseEntity<ResponseObject>> exportExcelAndZipPdf(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String wardId,
            @PathVariable @NotEmpty(message = "Loại sổ là bắt buộc") String registrationType,
            @PathVariable @NotEmpty(message = "Kích thước sổ là bắt buộc") String paperSize,
            @PathVariable @NotEmpty(message = "Quyển số là bắt buộc") String numberBook,
            @PathVariable @NotEmpty(message = "Năm mở sổ là bắt buộc") String numberBookYear
    ) {
        boolean isValidRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isValidRegistrationType) {
            throw new DataInputException("Loại sổ không tồn tại");
        }

        boolean isValidPaperSize = EPaperSize.checkValue(paperSize.toUpperCase());

        if (!isValidPaperSize) {
            throw new DataInputException("Kích thước sổ không tồn tại");
        }

        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> new DataInputException("Phường / Xã / Thị trấn không tồn tại"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        EPaperSize ePaperSize = EPaperSize.valueOf(paperSize.toUpperCase());

        User user = userService.getAuthenticatedUser();

        Optional<ExportHistory> exportHistoryOptional = exportHistoryService.findByAll(
                projectWard.getProject().getId(),
                projectWard.getId(),
                registrationType,
                paperSize,
                numberBookYear,
                numberBook,
                EExportStatus.PROCESSING
        );

        if (exportHistoryOptional.isPresent()) {
            throw new DataInputException("Tệp dữ liệu trích xuất excel và zip pdf này đang được xử lý");
        }

        exportHistoryService.asyncExportExcelAndZipPdf(
                projectWard,
                eRegistrationType,
                ePaperSize,
                numberBookYear,
                numberBook,
                user
        );

        return CompletableFuture.completedFuture(ResponseEntity.ok().body(ResponseObject.builder()
                .message("Trích xuất tệp excel và zip pdf thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build()));
    }

}
