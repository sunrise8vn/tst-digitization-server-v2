package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.project.ProjectNumberBookCreateDTO;
import com.tst.models.dtos.project.ProjectNumberBookTempDTO;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.entities.User;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.responses.ResponseObject;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectNumberBookTemp.IProjectNumberBookTempService;
import com.tst.services.projectRegistrationDate.IProjectRegistrationDateService;
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

    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;
    private final IProjectRegistrationDateService projectRegistrationDateService;
    private final IProjectNumberBookTempService projectNumberBookTempService;
    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    @PostMapping("/number-book")
    public ResponseEntity<ResponseObject> createProjectNumberBook(ProjectNumberBookCreateDTO projectNumberBookCreateDTO) {

        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateService.findById(
                projectNumberBookCreateDTO.getProjectRegistrationDateId()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Năm đăng ký không tồn tại");
        });

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProjectNumberBook projectNumberBook = projectNumberBookCreateDTO.toProjectNumberBook(projectRegistrationDate);
        projectNumberBook.setCreatedBy(user);
        projectNumberBook.setStatus(EProjectNumberBookStatus.NEW);

        projectNumberBookService.create(projectNumberBook, projectNumberBookCreateDTO.getCoverFile());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Create Project Number Book successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PostMapping("/number-book/upload-pdf")
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
                Long.parseLong(projectNumberBookTempDTO.getProjectNumberBookId()), EProjectNumberBookStatus.ACCEPT
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID quyển số không tồn tại");
        });

        List<String> failedFiles = projectNumberBookTempService.create(files, projectNumberBook.getProjectNumberBookCover().getFolderName(), projectNumberBook);

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
                .message("Accept Create Project Number Book successfully")
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
                .message("Cancel Create Project Number Book successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
