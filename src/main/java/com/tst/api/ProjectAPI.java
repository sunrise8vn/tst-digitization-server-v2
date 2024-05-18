package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.accessPoint.AccessPointRevokeDTO;
import com.tst.models.dtos.project.*;
import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.locationRegion.LocationResponse;
import com.tst.models.responses.project.ProjectResponse;
import com.tst.models.responses.project.RegistrationNumberBookResponse;
import com.tst.models.responses.project.RegistrationPointResponse;
import com.tst.services.accessPoint.IAccessPointService;
import com.tst.services.locationDistrict.ILocationDistrictService;
import com.tst.services.locationProvince.ILocationProvinceService;
import com.tst.services.locationWard.ILocationWardService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectNumberBookFile.IProjectNumberBookFileService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectUser.IProjectUserService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("${api.prefix}/projects")
@RequiredArgsConstructor
@Validated
public class ProjectAPI {

    private final IAccessPointService accessPointService;
    private final IProjectUserService projectUserService;
    private final IProjectService projectService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectWardService projectWardService;
    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;
    private final IProjectNumberBookFileService projectNumberBookFileService;
    private final IRegistrationTypeService registrationTypeService;
    private final ILocationProvinceService locationProvinceService;
    private final ILocationDistrictService locationDistrictService;
    private final ILocationWardService locationWardService;
    private final IUserService userService;

    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    @GetMapping("/get-all")
    public ResponseEntity<ResponseObject> getAllProjects() {
        User user = userService.getAuthenticatedUser();

        List<ProjectResponse> projectResponses = projectUserService.findAllProjectResponseByUser(user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(projectResponses)
                .build());
    }

    @PostMapping("/get-all-registration-point-by-project-and-user")
    public ResponseEntity<ResponseObject> getAllRegistrationPointByProject(
            @Validated @RequestBody ProjectDTO projectDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lấy danh sách địa điểm theo dự án không thành công")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        Project project = projectService.findById(
                Long.parseLong(projectDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<RegistrationPointResponse> registrationPointResponses = projectService.findAllRegistrationPointByProjectAndUser(project, user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách địa điểm thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationPointResponses)
                .build());
    }

    @GetMapping("/get-all-provinces-by-project/{projectId}")
    public ResponseEntity<ResponseObject> getAllProvincesByProject(
        @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<LocationResponse> locationResponses =  projectService.findAllProvincesByProjectAndUser(project, user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách tỉnh / thành phố theo dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationResponses)
                .build());
    }

    @GetMapping("/get-all-districts-by-project-and-province/{projectId}/{provinceId}")
    public ResponseEntity<ResponseObject> getAllDistrictsByProjectAndProvince(
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
            throw new DataInputException("Tỉnh / thành phố không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<LocationResponse> locationResponses =  projectService.findAllDistrictsByProjectAndProvinceAndUser(
                project,
                projectProvince,
                user
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách quận / huyện / thành phố theo dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationResponses)
                .build());
    }

    @GetMapping("/get-all-wards-by-project-and-district/{projectId}/{districtId}")
    public ResponseEntity<ResponseObject> getAllWardsByProjectAndDistrict(
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
            throw new DataInputException("Quận / huyện / thành phố không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<LocationResponse> locationResponses =  projectService.findAllWardsByProjectAndDistrictAndUser(
                project,
                projectDistrict,
                user
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách phường / xã / thị trấn theo dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationResponses)
                .build());
    }

    @GetMapping("/get-all-number-books-by-project-and-province/{projectId}/{provinceId}")
    public ResponseEntity<ResponseObject> getAllNumberBooksByProjectAndProvince(
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

        List<RegistrationNumberBookResponse> registrationNumberBookResponses =  projectService.findAllNumberBooksByProjectAndProjectProvince(
                project,
                projectProvince
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ của dự án theo tỉnh / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationNumberBookResponses)
                .build());
    }

    @GetMapping("/get-all-number-books-by-project-and-district/{projectId}/{districtId}")
    public ResponseEntity<ResponseObject> getAllNumberBooksByProjectAndDistrict(
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

        List<RegistrationNumberBookResponse> registrationNumberBookResponses =  projectService.findAllNumberBooksByProjectAndProjectDistrict(
                project,
                projectDistrict
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ của dự án theo quận / huyện / thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationNumberBookResponses)
                .build());
    }

    @GetMapping("/get-all-number-books-by-project-and-ward/{projectId}/{wardId}")
    public ResponseEntity<ResponseObject> getAllNumberBooksByProjectAndWard(
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

        List<RegistrationNumberBookResponse> registrationNumberBookResponses =  projectService.findAllNumberBooksByProjectAndProjectWard(
                project,
                projectWard
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ của dự án theo phường / xã / thị trấn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationNumberBookResponses)
                .build());
    }

    @PostMapping("/verify-project-by-user")
    public ResponseEntity<ResponseObject> verifyProjectByUser(
            @Validated @RequestBody ProjectDTO projectDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Xác thực dự án không thành công")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        Project project = projectService.findById(
                Long.parseLong(projectDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        Optional<ProjectResponse> projectResponse = projectService.findProjectResponseByProjectAndUser(project, user);

        if (projectResponse.isEmpty()) {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác thực dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(projectResponse)
                .build());
    }


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
           throw new DataInputException("Dự án không tồn tại");
        });

        LocationProvince locationProvince = locationProvinceService.findById(
                Long.parseLong(registrationPointDTO.getProvince_id())
        ).orElseThrow(() -> {
            throw new DataInputException("Tỉnh/thành phố không tồn tại");
        });

        LocationDistrict locationDistrict = locationDistrictService.findById(
                Long.parseLong(registrationPointDTO.getDistrict_id())
        ).orElseThrow(() -> {
            throw new DataInputException("Thành phố/quận/huyện không tồn tại");
        });

        LocationWard locationWard = locationWardService.findById(
                Long.parseLong(registrationPointDTO.getWard_id())
        ).orElseThrow(() -> {
            throw new DataInputException("Phường/xã không tồn tại");
        });

        projectService.createRegistrationPoint(
                project,
                locationProvince,
                locationDistrict,
                locationWard
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Khởi tạo điểm đăng ký hộ tịch thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PostMapping("/registration-number-book")
    public ResponseEntity<ResponseObject> createRegistrationNumberBook(
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

//    @PostMapping("/number-book")
//    public ResponseEntity<ResponseObject> createProjectNumberBook(
//            ProjectNumberBookCreateDTO projectNumberBookCreateDTO
//    ) {
//
//        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateService.findById(
//                projectNumberBookCreateDTO.getProject_registration_date_id()
//        ).orElseThrow(() -> {
//            throw new DataInputException("Năm đăng ký không tồn tại");
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

    // Upload pdf lên thư mục sổ hộ tịch
    @PostMapping("/number-book-file/upload-pdf")
    public ResponseEntity<ResponseObject> uploadPdfFilesProjectNumberBook(
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
        ).orElseThrow(() -> {
            throw new DataInputException("ID quyển số không tồn tại");
        });

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

    // Phân phối biểu mẫu cho người dùng
    @PostMapping("/assign-extract-form")
    public ResponseEntity<ResponseObject> assignExtractFormToUser(
            @Validated @RequestBody AssignExtractFormDTO assignExtractFormDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi gán phiếu nhập cho người dùng")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (Long.parseLong(assignExtractFormDTO.getTotal_count()) < assignExtractFormDTO.getUsers().size()) {
            throw new DataInputException("Tổng số phiếu phân phối phải lớn hơn hoặc bằng tổng số người dùng");
        }

        Project project = projectService.findById(
                Long.parseLong(assignExtractFormDTO.getProject_id())
        ).orElseThrow(() -> {
           throw new DataInputException("ID dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        projectUserService.findByProjectAndUser(project, user).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        if (assignExtractFormDTO.getUsers().size() < 2) {
            throw new DataInputException("Số người dùng được phân phối ít nhất là 2");
        }

        List<User> users = new ArrayList<>();

        for (String username : assignExtractFormDTO.getUsers()) {
            Optional<User> userOptional = userService.findByUsername(username);

            if (userOptional.isEmpty()) {
                throw new PermissionDenyException("Tài khoản " + username + " không tồn tại");
            }

            if (users.contains(userOptional.get())) {
                throw new DataInputException("Danh sách người dùng không được trùng nhau");
            }

            projectUserService.findByProjectAndUser(project, userOptional.get()).orElseThrow(() -> {
                throw new PermissionDenyException("Tài khoản " + username + " không thuộc dự án này");
            });

            users.add(userOptional.get());
        }

        projectService.assignExtractFormToUser(
                project,
                Long.parseLong(assignExtractFormDTO.getTotal_count()),
                users
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Gán phiếu nhập cho người dùng thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // So sánh tự động các biểu mẫu trường ngắn và trường dài đã nhập
    @PostMapping("/auto-compare-extract-short-full/{accessPointId}")
    public ResponseEntity<ResponseObject> assignExtractFormToUser(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID điểm truy cập phải là một số") String accessPointId
    ) {
        AccessPoint accessPoint = accessPointService.findById(
                Long.parseLong(accessPointId)
        ).orElseThrow(() -> {
            throw new DataInputException("ID điểm truy cập không tồn tại");
        });

        projectService.autoCompareExtractShortFull(accessPoint);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Đối sánh các trường nhập liệu của các biểu mẫu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // Thu hồi biểu mẫu chưa nhập
    @PostMapping("/revoke-extract-form")
    public ResponseEntity<ResponseObject> revokeExtractForm(
            @Validated @RequestBody AccessPointRevokeDTO accessPointRevokeDTO,
            BindingResult result
    ) {

        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi thu hồi các biểu mẫu chưa nhập")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        AccessPoint accessPoint = accessPointService.findById(
                Long.parseLong(accessPointRevokeDTO.getAccess_point_id())
        ).orElseThrow(() -> {
            throw new DataInputException("ID điểm truy cập không tồn tại");
        });

        accessPointService.revokeExtractForm(accessPoint, Long.parseLong(accessPointRevokeDTO.getTotal_count_revoke()));

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Thu hồi các biểu mẫu chưa nhập thành công")
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
                Long.parseLong(projectNumberBookId),
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
    @PatchMapping("/number-book/cancel/{projectNumberBookId}")
    public ResponseEntity<ResponseObject> cancelCreateProjectNumberBook(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID quyển số phải là một số") String projectNumberBookId
    ) throws IOException {

        ProjectNumberBook projectNumberBook = projectNumberBookService.findByIdAndStatus(
                Long.parseLong(projectNumberBookId),
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

    // Kiểm tra và đặt tên file pdf
    @PatchMapping("/number-book-file/organization")
    public ResponseEntity<ResponseObject> organizationPdfFileProjectNumberBookFile(
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
        ).orElseThrow(() -> {
            throw new DataInputException("ID tập tin không tồn tại");
        });

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
    @PatchMapping("/number-book-file/approve/{id}")
    public ResponseEntity<ResponseObject> approvePdfFileProjectNumberBookFile(
            @PathVariable @Pattern(regexp = "\\d+", message = "ID tập tin phải là một số") String id
    ) throws IOException {

        ProjectNumberBookFile projectNumberBookFile = projectNumberBookFileService.findByIdAndStatus(
                Long.parseLong(id),
                EProjectNumberBookFileStatus.ORGANIZED
        ).orElseThrow(() -> {
            throw new DataInputException("ID tập tin không tồn tại");
        });

        projectNumberBookFileService.approve(projectNumberBookFile);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Tổ chức tập tin được chấp thuận thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
