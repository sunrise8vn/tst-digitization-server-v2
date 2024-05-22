package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.project.RegistrationNumberBookDTO;
import com.tst.models.entities.*;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.project.NumberBookNewResponse;
import com.tst.models.responses.project.NumberBookPendingResponse;
import com.tst.models.responses.project.NumberBookVerifyResponse;
import com.tst.models.responses.project.NumberBookApprovedResponse;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.services.registrationType.IRegistrationTypeService;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("${api.prefix}/number-books")
@RequiredArgsConstructor
@Validated
public class NumberBookAPI {

    private final IUserService userService;
    private final IRegistrationTypeService registrationTypeService;
    private final IProjectService projectService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectWardService projectWardService;
    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;

    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    @GetMapping("/get-new-by-project-and-id/{projectId}/{numberBookId}")
    public ResponseEntity<ResponseObject> getNewByProjectAndId(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID sổ phải là một số") String numberBookId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectNumberBook projectNumberBook = projectNumberBookService.findById(
                Long.parseLong(numberBookId)
        ).orElseThrow(() -> {
            throw new DataInputException("Sổ không tồn tại");
        });

        Optional<NumberBookPendingResponse> numberBookPendingResponse =  projectNumberBookService.findNewByProjectAndId(
                project,
                projectNumberBook.getId()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thông tin sổ mới của dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookPendingResponse)
                .build());
    }

    @GetMapping("/get-next-new-by-project-and-id/{projectId}/{numberBookId}")
    public ResponseEntity<ResponseObject> getNextNewByProjectAndId(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID sổ phải là một số") String numberBookId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectNumberBook projectNumberBook = projectNumberBookService.findById(
                Long.parseLong(numberBookId)
        ).orElseThrow(() -> {
            throw new DataInputException("Sổ không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        Optional<ProjectNumberBook> projectNumberBookOptional = projectNumberBookService.findNextNewByProjectAndUserIdAndId(
                project.getId(),
                user.getId(),
                projectNumberBook.getId()
        );

        if (projectNumberBookOptional.isEmpty()) {
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Lấy thông tin sổ mới của dự án thành công")
                    .status(HttpStatus.NO_CONTENT.value())
                    .statusText(HttpStatus.NO_CONTENT)
                    .build());
        }

        projectNumberBook = projectNumberBookOptional.get();

        NumberBookPendingResponse numberBookPendingResponse = new NumberBookPendingResponse()
                .setNumberBookId(projectNumberBook.getId())
                .setProvinceName(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getProjectProvince()
                        .getName())
                .setDistrictName(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getName())
                .setWardName(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getName())
                .setRegistrationType(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getCode())
                .setPaperSize(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getCode())
                .setRegistrationDate(projectNumberBook
                        .getProjectRegistrationDate()
                        .getCode())
                .setNumberBookCode(projectNumberBook
                        .getCode())
                .setFolderPath(projectNumberBook
                        .getProjectNumberBookCover()
                        .getFolderPath())
                .setFileName(projectNumberBook
                        .getProjectNumberBookCover()
                        .getFileName())
                .setStatus(projectNumberBook.getStatus());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thông tin sổ mới của dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookPendingResponse)
                .build());
    }

    @GetMapping("/get-all-new-by-project-and-province/{projectId}/{provinceId}")
    public ResponseEntity<ResponseObject> getAllNewByProjectAndProvince(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tỉnh / thành phố phải là một số") String provinceId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectProvince projectProvince = projectProvinceService.findById(
                Long.parseLong(provinceId)
        ).orElseThrow(() -> {
            throw new DataInputException("Tỉnh / Thành phố không tồn tại");
        });

        List<NumberBookNewResponse> numberBookNewResponses =  projectService.findAllNewNumberBooksByProjectAndProjectProvince(
                project,
                projectProvince
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ mới của dự án theo tỉnh / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookNewResponses)
                .build());
    }

    @GetMapping("/get-all-new-by-project-and-district/{projectId}/{districtId}")
    public ResponseEntity<ResponseObject> getAllNewByProjectAndDistrict(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID quận / huyện / thành phố phải là một số") String districtId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectDistrict projectDistrict = projectDistrictService.findById(
                Long.parseLong(districtId)
        ).orElseThrow(() -> {
            throw new DataInputException("Quận / Huyện / Thành phố không tồn tại");
        });

        List<NumberBookNewResponse> numberBookNewResponses =  projectService.findAllNewNumberBooksByProjectAndProjectDistrict(
                project,
                projectDistrict
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ mới của dự án theo quận / huyện / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookNewResponses)
                .build());
    }

    @GetMapping("/get-all-new-by-project-and-ward/{projectId}/{wardId}")
    public ResponseEntity<ResponseObject> getAllNewByProjectAndWard(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID phường / xã / thị trấn phải là một số") String wardId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> {
            throw new DataInputException("Phường / Xã / Thị trấn không tồn tại");
        });

        List<NumberBookNewResponse> numberBookNewResponses =  projectService.findAllNewNumberBooksByProjectAndProjectWard(
                project,
                projectWard
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ mới của dự án theo phường / xã / thị trấn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookNewResponses)
                .build());
    }


    @GetMapping("/get-all-approved-by-project-and-province/{projectId}/{provinceId}")
    public ResponseEntity<ResponseObject> getAllApprovedByProjectAndProvince(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID tỉnh / thành phố phải là một số") String provinceId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectProvince projectProvince = projectProvinceService.findById(
                Long.parseLong(provinceId)
        ).orElseThrow(() -> {
            throw new DataInputException("Tỉnh / Thành phố không tồn tại");
        });

        List<NumberBookApprovedResponse> numberBookApprovedResponses =  projectService.findAllApprovedNumberBooksByProjectAndProjectProvince(
                project,
                projectProvince
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ của dự án theo tỉnh / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookApprovedResponses)
                .build());
    }

    @GetMapping("/get-all-approved-by-project-and-district/{projectId}/{districtId}")
    public ResponseEntity<ResponseObject> getAllApprovedByProjectAndDistrict(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID quận / huyện / thành phố phải là một số") String districtId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectDistrict projectDistrict = projectDistrictService.findById(
                Long.parseLong(districtId)
        ).orElseThrow(() -> {
            throw new DataInputException("Quận / Huyện / Thành phố không tồn tại");
        });

        List<NumberBookApprovedResponse> numberBookApprovedResponses =  projectService.findAllApprovedNumberBooksByProjectAndProjectDistrict(
                project,
                projectDistrict
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ của dự án theo quận / huyện / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookApprovedResponses)
                .build());
    }

    @GetMapping("/get-all-approved-by-project-and-ward/{projectId}/{wardId}")
    public ResponseEntity<ResponseObject> getAllApprovedByProjectAndWard(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID phường / xã / thị trấn phải là một số") String wardId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> {
            throw new DataInputException("Phường / Xã / Thị trấn không tồn tại");
        });

        List<NumberBookApprovedResponse> numberBookApprovedResponses =  projectService.findAllApprovedNumberBooksByProjectAndProjectWard(
                project,
                projectWard
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ của dự án theo phường / xã / thị trấn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookApprovedResponses)
                .build());
    }

    @GetMapping("/verify/{projectId}/{numberBookId}")
    public ResponseEntity<ResponseObject> verify(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "\\d+", message = "ID quyển số phải là một số") String numberBookId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByProjectAndId(
                project,
                Long.parseLong(numberBookId)
        ).orElseThrow(() -> {
            throw new DataInputException("Quyển sổ không tồn tại");
        });

        if (!projectNumberBook.getStatus().equals(EProjectNumberBookStatus.ACCEPT)) {
            throw new DataInputException("Quyển sổ này chưa được phê duyệt để sử dụng");
        }

        NumberBookVerifyResponse numberBookVerifyResponse = new NumberBookVerifyResponse()
                .setId(projectNumberBook.getId())
                .setProjectName(project.getName())
                .setProvinceName(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getProjectProvince()
                        .getName())
                .setDistrictName(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getProjectDistrict()
                        .getName())
                .setWardName(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getProjectWard()
                        .getName())
                .setRegistrationType(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getProjectRegistrationType()
                        .getCode())
                .setPaperSize(projectNumberBook
                        .getProjectRegistrationDate()
                        .getProjectPaperSize()
                        .getCode())
                .setRegistrationDate(projectNumberBook
                        .getProjectRegistrationDate()
                        .getCode())
                .setNumberBookCode(projectNumberBook
                        .getCode());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác thực sổ của dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookVerifyResponse)
                .build());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> create(
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

        if (!ERegistrationType.checkValue(registrationNumberBookDTO.getRegistration_type_code())) {
            throw new DataInputException("Loại sổ đăng ký không hợp lệ");
        }

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationNumberBookDTO.getRegistration_type_code());

        RegistrationType registrationType = registrationTypeService.findByCode(
                eRegistrationType
        ).orElseThrow(() -> {
            throw new DataInputException("Loại sổ đăng ký không tồn tại");
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

    // FE quản lý kiểm tra màn hình dữ liệu ảnh bìa khớp với cấu trúc thư mục thì chấp nhận
    @PatchMapping("/accept/{id}")
    public ResponseEntity<ResponseObject> accept(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID quyển số phải là một số") String id
    ) {

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID quyển số không tồn tại");
        });

        projectNumberBookService.updateAccept(projectNumberBook);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Quyển sổ " + projectNumberBook.getCode() + " được đưa vào sử dụng thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // FE quản lý kiểm tra màn hình dữ liệu ảnh bìa không khớp với cấu trúc thư mục thì hủy bỏ và xóa thư mục ảnh bìa
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<ResponseObject> cancel(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID quyển số phải là một số") String id
    ) throws IOException {

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID quyển số không tồn tại");
        });

        projectNumberBookService.updateCancel(projectNumberBook);

        // Đăng ký mới nên folder chỉ có 1 file cover, được xóa hết thư mục mới tạo
        projectNumberBookCoverService.deleteCoverDirectory(projectNumberBook.getProjectNumberBookCover());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Hủy đăng ký cho quyển sổ " + projectNumberBook.getCode() + " thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
