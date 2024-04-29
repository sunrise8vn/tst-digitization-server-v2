package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.project.*;
import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.enums.EProjectNumberBookTempStatus;
import com.tst.models.responses.ResponseObject;
import com.tst.services.locationDistrict.ILocationDistrictService;
import com.tst.services.locationProvince.ILocationProvinceService;
import com.tst.services.locationWard.ILocationWardService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectNumberBookTemp.IProjectNumberBookTempService;
import com.tst.services.projectRegistrationDate.IProjectRegistrationDateService;
import com.tst.services.projectRegistrationType.IProjectRegistrationTypeService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.services.registrationType.IRegistrationTypeService;
import com.tst.utils.AppUtils;
import com.tst.utils.FileUtils;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/projects")
@RequiredArgsConstructor
@Validated
public class ProjectAPI {

    private final IProjectService projectService;
    private final IProjectWardService projectWardService;
    private final IProjectRegistrationTypeService projectRegistrationTypeService;
    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;
    private final IProjectRegistrationDateService projectRegistrationDateService;
    private final IProjectNumberBookTempService projectNumberBookTempService;
    private final IRegistrationTypeService registrationTypeService;
    private final ILocationProvinceService locationProvinceService;
    private final ILocationDistrictService locationDistrictService;
    private final ILocationWardService locationWardService;

    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    @PostMapping("/registration-point")
    public ResponseEntity<ResponseObject> createRegistrationPoint(
            @Validated @RequestBody RegistrationPointDTO registrationPointDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi khởi tạo điểm đăng ký hộ tịch")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        Project project = projectService.findById(
                Long.parseLong(registrationPointDTO.getProject_id())
        ).orElseThrow(() -> {
           throw new DataNotFoundException("Dự án không tồn tại");
        });

        LocationProvince locationProvince = locationProvinceService.findById(
                Long.parseLong(registrationPointDTO.getProvince_id())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Tỉnh/thành phố không tồn tại");
        });

        LocationDistrict locationDistrict = locationDistrictService.findById(
                Long.parseLong(registrationPointDTO.getDistrict_id())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Thành phố/quận/huyện không tồn tại");
        });

        LocationWard locationWard = locationWardService.findById(
                Long.parseLong(registrationPointDTO.getWard_id())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Phường/xã không tồn tại");
        });

        projectService.createRegistrationPoint(project, locationProvince, locationDistrict, locationWard);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Khởi tạo điểm đăng ký hộ tịch thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PostMapping("/registration-number-book")
    public ResponseEntity<ResponseObject> createRegistrationPoint(
            @Validated RegistrationNumberBookDTO registrationNumberBookDTO,
            BindingResult result
    ) throws IOException {

        if (registrationNumberBookDTO.getCover_file() == null) {
            throw new DataInputException("Vui lòng chọn tệp PDF");
        }

        if (!fileUtils.isPDFUsingTika(registrationNumberBookDTO.getCover_file())) {
            throw new DataInputException("Vui lòng gửi đúng tệp PDF");
        }

        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi khởi tạo điểm đăng ký hộ tịch")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(registrationNumberBookDTO.getWard_id())
        ).orElseThrow(() -> {
           throw new DataInputException("ID phường/xã không tồn tại");
        });

        RegistrationType registrationType = registrationTypeService.findByCode(
                registrationNumberBookDTO.getRegistration_type_code()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Loại sổ đăng ký không tồn tại");
        });

        if (!EPaperSize.checkValue(registrationNumberBookDTO.getPaper_size_code())) {
            throw new DataInputException("Khổ giấy không hợp lệ");
        }

        EPaperSize ePaperSize = EPaperSize.valueOf(registrationNumberBookDTO.getPaper_size_code());

        projectService.createRegistrationNumberBook(
                projectWard,
                registrationType,
                ePaperSize,
                registrationNumberBookDTO.getRegistration_date_code(),
                registrationNumberBookDTO.getNumber_book_code(),
                registrationNumberBookDTO.getCover_file()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Khởi tạo sổ hộ tịch thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

//    @PostMapping("/number-book")
//    public ResponseEntity<ResponseObject> createProjectNumberBook(
//            ProjectNumberBookCreateDTO projectNumberBookCreateDTO
//    ) {
//
//        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateService.findById(
//                projectNumberBookCreateDTO.getProject_registration_date_id()
//        ).orElseThrow(() -> {
//            throw new DataNotFoundException("Năm đăng ký không tồn tại");
//        });
//
//        ProjectNumberBook projectNumberBook = projectNumberBookCreateDTO.toProjectNumberBook(projectRegistrationDate);
//
//        projectNumberBookService.create(projectNumberBook, projectNumberBookCreateDTO.getCover_file());
//
//        return ResponseEntity.ok().body(ResponseObject.builder()
//                .message("Create Project Number Book successfully")
//                .status(HttpStatus.OK.value())
//                .statusText(HttpStatus.OK)
//                .build());
//    }

    @PostMapping("/number-book-temp/upload-pdf")
    public ResponseEntity<ResponseObject> uploadPdfFilesProjectNumberBook(
            @Validated ProjectNumberBookTempDTO projectNumberBookTempDTO,
            BindingResult result
    ) throws IOException {

        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi tải tệp lên hệ thống")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (projectNumberBookTempDTO.getFiles() == null) {
            throw new DataInputException("Vui lòng chọn các tệp PDF");
        }

        List<MultipartFile> files = projectNumberBookTempDTO.getFiles();
        List<String> nonPdfFiles = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            if (!fileUtils.isPDFUsingTika(multipartFile)) {
                nonPdfFiles.add(multipartFile.getOriginalFilename());
            }
        }

        if (!nonPdfFiles.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Các tệp sau không phải là tệp PDF")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(nonPdfFiles)
                    .build());
        }

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByIdAndStatus(
                Long.parseLong(projectNumberBookTempDTO.getNumber_book_id()),
                EProjectNumberBookStatus.ACCEPT
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID quyển số không tồn tại");
        });

        List<String> failedFiles = projectNumberBookTempService.create(files, projectNumberBook.getProjectNumberBookCover().getFolderPath(), projectNumberBook);

        if (!failedFiles.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Không thể lưu các tệp này")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(failedFiles)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Tất cả tệp đã được lưu thành công.")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // FE quản lý kiểm tra màn hình dữ liệu ảnh bìa khớp với cấu trúc thư mục thì chấp nhận
    @PatchMapping("/number-book/accept/{projectNumberBookId}")
    public ResponseEntity<ResponseObject> acceptCreateProjectNumberBook(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID quyển số phải là một số") String projectNumberBookId
    ) {

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByIdAndStatus(
                Long.parseLong(projectNumberBookId), EProjectNumberBookStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID quyển số không tồn tại");
        });

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        projectNumberBook.setUpdatedBy(user);
        projectNumberBook.setStatus(EProjectNumberBookStatus.ACCEPT);

        projectNumberBookService.update(projectNumberBook);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Quyển sổ " + projectNumberBook.getCode() + " được đưa vào sử dụng thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // FE quản lý kiểm tra màn hình dữ liệu ảnh bìa không khớp với cấu trúc thư mục thì hủy bỏ và xóa thư mục ảnh bìa
    @PatchMapping("/number-book/cancel/{projectNumberBookId}")
    public ResponseEntity<ResponseObject> cancelCreateProjectNumberBook(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID quyển số phải là một số") String projectNumberBookId
    ) throws IOException {

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByIdAndStatus(
                Long.parseLong(projectNumberBookId), EProjectNumberBookStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID quyển số không tồn tại");
        });

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        projectNumberBook.setUpdatedBy(user);
        projectNumberBook.setStatus(EProjectNumberBookStatus.CANCEL);

        projectNumberBookService.update(projectNumberBook);

        // Đăng ký mới nên folder chỉ có 1 file cover, được xóa hết thư mục mới tạo
        projectNumberBookCoverService.deleteCoverDirectory(projectNumberBook.getProjectNumberBookCover());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Hủy đăng ký cho quyển sổ " + projectNumberBook.getCode() + " thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // Kiểm tra và đặt tên file pdf
    @PatchMapping("/number-book-temp/organization")
    public ResponseEntity<ResponseObject> organizationPdfFileProjectNumberBookTemp(
            @Validated @RequestBody ProjectNumberBookTempOrganizationDTO projectNumberBookTempOrganizationDTO,
            BindingResult result
    ) throws IOException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi tổ chức tập tin")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        ProjectNumberBookTemp projectNumberBookTemp = projectNumberBookTempService.findByIdAndStatus(
                Long.parseLong(projectNumberBookTempOrganizationDTO.getId()),
                EProjectNumberBookTempStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID tập tin không tồn tại");
        });

        projectNumberBookTempService.organization(
                projectNumberBookTemp,
                projectNumberBookTempOrganizationDTO.getDay_month_year(),
                projectNumberBookTempOrganizationDTO.getNumber()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Tập tin được tổ chức thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // Kiểm tra và chấp nhận tổ chức file đúng với yêu cầu, chuyển file vào đúng thư mục
    @PatchMapping("/number-book-temp/approve/{id}")
    public ResponseEntity<ResponseObject> approvePdfFileProjectNumberBookTemp(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID tập tin phải là một số") String id
    ) throws IOException {

        ProjectNumberBookTemp projectNumberBookTemp = projectNumberBookTempService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookTempStatus.ORGANIZED
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID tập tin không tồn tại");
        });

        projectNumberBookTempService.approve(projectNumberBookTemp);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Tổ chức tập tin được chấp thuận thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
