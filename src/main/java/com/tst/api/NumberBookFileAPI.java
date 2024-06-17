package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.project.ProjectNumberBookFileDTO;
import com.tst.models.dtos.project.ProjectNumberBookFileOrganizationDTO;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.entities.ProjectWard;
import com.tst.models.entities.User;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.project.NumberBookFileListResponse;
import com.tst.models.responses.project.NumberBookFileResponse;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookFile.IProjectNumberBookFileService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import com.tst.utils.FileUtils;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/number-book-files")
@RequiredArgsConstructor
@Validated
public class NumberBookFileAPI {

    private final IUserService userService;
    private final IProjectWardService projectWardService;
    private final IProjectNumberBookFileService projectNumberBookFileService;
    private final IProjectNumberBookService projectNumberBookService;

    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    // Lấy thông tin tập tin mới
    @GetMapping("/get-new/{id}")
    public ResponseEntity<ResponseObject> getNew(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tập tin phải là một số") String id
    ) {
        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookFileStatus.NEW
        ).orElseThrow(() -> new DataInputException("Tập tin không tồn tại"));

        NumberBookFileResponse numberBookFileResponse = new NumberBookFileResponse()
                .setId(projectNumberBookFile.getId())
                .setProvinceName(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getProjectProvince()
                        .getName())
                .setDistrictName(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getName())
                .setWardName(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getName())
                .setRegistrationType(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getCode())
                .setPaperSize(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getCode())
                .setFolderPath(projectNumberBookFile.getFolderPath())
                .setFileName(projectNumberBookFile.getFileName())
                .setNumber(projectNumberBookFile.getNumber())
                .setDayMonthYear(projectNumberBookFile.getRegistrationDate())
                .setStatus(projectNumberBookFile.getStatus());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thông tin tập tin mới thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileResponse)
                .build());
    }

    // Lấy thông tin tập tin chờ xét duyệt
    @GetMapping("/get-organized/{id}")
    public ResponseEntity<ResponseObject> getOrganized(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tập tin phải là một số") String id
    ) {
        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookFileStatus.ORGANIZED
        ).orElseThrow(() -> new DataInputException("Tập tin không tồn tại"));

        NumberBookFileResponse numberBookFileResponse = new NumberBookFileResponse()
                .setId(projectNumberBookFile.getId())
                .setProvinceName(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getProjectProvince()
                        .getName())
                .setDistrictName(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getName())
                .setWardName(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getName())
                .setRegistrationType(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getCode())
                .setPaperSize(projectNumberBookFile
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getCode())
                .setFolderPath(projectNumberBookFile.getFolderPath())
                .setFileName(projectNumberBookFile.getFileName())
                .setNumber(projectNumberBookFile.getNumber())
                .setDayMonthYear(projectNumberBookFile.getRegistrationDate())
                .setStatus(projectNumberBookFile.getStatus());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thông tin tập tin chờ xét duyệt thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileResponse)
                .build());
    }

    // Lấy thông tin tập tin mới tiếp theo
    @GetMapping("/get-next-new/{id}")
    public ResponseEntity<ResponseObject> getNextNew(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tập tin phải là một số") String id
    ) {
        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findById(
                Long.parseLong(id)
        ).orElseThrow(() -> new DataInputException("Tập tin không tồn tại"));

        ProjectNumberBookFile projectNumberBookFileNext = projectNumberBookFileService.findNextByIdAndStatus(
                projectNumberBookFile.getProject().getId(),
                Long.parseLong(id),
                EProjectNumberBookFileStatus.NEW.getValue()
        ).orElseThrow(() -> new DataInputException("Không có tập tin tiếp theo"));

        NumberBookFileResponse numberBookFileResponse = new NumberBookFileResponse()
                .setId(projectNumberBookFileNext.getId())
                .setProvinceName(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getProjectProvince()
                        .getName())
                .setDistrictName(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getName())
                .setWardName(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getName())
                .setRegistrationType(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getCode())
                .setPaperSize(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getCode())
                .setFolderPath(projectNumberBookFileNext.getFolderPath())
                .setFileName(projectNumberBookFileNext.getFileName())
                .setStatus(projectNumberBookFileNext.getStatus())
                .setNumber(projectNumberBookFileNext.getNumber())
                .setDayMonthYear(projectNumberBookFileNext.getRegistrationDate());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thông tin tập tin mới tiếp theo thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileResponse)
                .build());
    }

    // Lấy thông tin tập tin chờ xét duyệt tiếp theo
    @GetMapping("/get-next-organized/{id}")
    public ResponseEntity<ResponseObject> getNextOrganized(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tập tin phải là một số") String id
    ) {
        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findById(
                Long.parseLong(id)
        ).orElseThrow(() -> new DataInputException("Tập tin không tồn tại"));

        ProjectNumberBookFile projectNumberBookFileNext = projectNumberBookFileService.findNextByIdAndStatus(
                projectNumberBookFile.getProject().getId(),
                Long.parseLong(id),
                EProjectNumberBookFileStatus.ORGANIZED.getValue()
        ).orElseThrow(() -> new DataInputException("Không có tập tin tiếp theo"));

        NumberBookFileResponse numberBookFileResponse = new NumberBookFileResponse()
                .setId(projectNumberBookFileNext.getId())
                .setProvinceName(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getProjectProvince()
                        .getName())
                .setDistrictName(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getName())
                .setWardName(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getName())
                .setRegistrationType(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getCode())
                .setPaperSize(projectNumberBookFileNext
                        .getProjectNumberBook()
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getCode())
                .setFolderPath(projectNumberBookFileNext.getFolderPath())
                .setFileName(projectNumberBookFileNext.getFileName())
                .setStatus(projectNumberBookFileNext.getStatus())
                .setNumber(projectNumberBookFileNext.getNumber())
                .setDayMonthYear(projectNumberBookFileNext.getRegistrationDate());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thông tin tập tin chờ xét duyệt tiếp theo thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileResponse)
                .build());
    }

    // Lấy danh sách tập tin mới
    @GetMapping("/get-all-new/{projectId}/{wardId}")
    public ResponseEntity<ResponseObject> getAllNew(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID phường / xã / thị trấn phải là một số") String wardId
    ) {
        ProjectWard projectWard = projectWardService.findByIdAndProjectId(
                Long.parseLong(wardId),
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Phường / xã / thị trấn không thuộc dự án này"));

        List<NumberBookFileListResponse> numberBookFileListResponses = projectNumberBookFileService.findAllNumberBookFileByStatus(
                projectWard,
                EProjectNumberBookFileStatus.NEW
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách tập tin mới thành công.")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileListResponses)
                .build());
    }

    // Lấy danh sách tập tin chờ xét duyệt
    @GetMapping("/get-all-organized/{projectId}/{wardId}")
    public ResponseEntity<ResponseObject> getAllOrganized(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID phường / xã / thị trấn phải là một số") String wardId
    ) {
        ProjectWard projectWard = projectWardService.findByIdAndProjectId(
                Long.parseLong(wardId),
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Phường / xã / thị trấn không thuộc dự án này"));

        List<NumberBookFileListResponse> numberBookFileListResponses = projectNumberBookFileService.findAllNumberBookFileByStatus(
                projectWard,
                EProjectNumberBookFileStatus.ORGANIZED
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách tập tin chờ xét duyệt thành công.")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileListResponses)
                .build());
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<ResponseObject> uploadPdfFiles(
            @Validated ProjectNumberBookFileDTO projectNumberBookFileDTO,
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

        if (projectNumberBookFileDTO.getFiles() == null) {
            throw new DataInputException("Vui lòng chọn các tệp PDF");
        }

        List<MultipartFile> files = projectNumberBookFileDTO.getFiles();
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
                Long.parseLong(projectNumberBookFileDTO.getNumber_book_id()),
                EProjectNumberBookStatus.ACCEPT
        ).orElseThrow(() -> new DataInputException("ID quyển số không tồn tại"));

        List<String> failedFiles = projectNumberBookFileService.create(
                files,
                projectNumberBook.getProjectNumberBookCover().getFolderPath(),
                projectNumberBook
        );

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

    // Kiểm tra và đặt tên file pdf
    @PatchMapping("/organization")
    public ResponseEntity<ResponseObject> organizationPdfFile(
            @Validated @RequestBody ProjectNumberBookFileOrganizationDTO projectNumberBookFileOrganizationDTO,
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

        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findByIdAndStatus(
                Long.parseLong(projectNumberBookFileOrganizationDTO.getId()),
                EProjectNumberBookFileStatus.NEW
        ).orElseThrow(() -> new DataInputException("ID tập tin không tồn tại"));

        projectNumberBookFileService.organization(
                projectNumberBookFile,
                projectNumberBookFileOrganizationDTO.getDay_month_year(),
                projectNumberBookFileOrganizationDTO.getNumber()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Tập tin được tổ chức thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // Kiểm tra và chấp nhận tổ chức file đúng với yêu cầu,
    // chuyển file vào đúng thư mục,
    // tạo mới record vào 2 bảng tương ứng với RegistrationType
    @PatchMapping("/approve/{id}")
    public ResponseEntity<ResponseObject> approvePdfFile(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID tập tin phải là một số") String id
    ) throws IOException {

        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookFileStatus.ORGANIZED
        ).orElseThrow(() -> new DataInputException("ID tập tin không tồn tại"));

        projectNumberBookFileService.approve(projectNumberBookFile);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Tổ chức tập tin được chấp thuận thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
