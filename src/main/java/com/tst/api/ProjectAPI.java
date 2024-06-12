package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.accessPoint.AccessPointRevokeDTO;
import com.tst.models.dtos.project.*;
import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.enums.ETableName;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.locationRegion.LocationResponse;
import com.tst.models.responses.project.*;
import com.tst.models.responses.statistic.StatisticProjectResponse;
import com.tst.services.accessPoint.IAccessPointService;
import com.tst.services.locationDistrict.ILocationDistrictService;
import com.tst.services.locationProvince.ILocationProvinceService;
import com.tst.services.locationWard.ILocationWardService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectPaperSize.IProjectPaperSizeService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectRegistrationDate.IProjectRegistrationDateService;
import com.tst.services.projectRegistrationType.IProjectRegistrationTypeService;
import com.tst.services.projectUser.IProjectUserService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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
    private final IProjectRegistrationTypeService projectRegistrationTypeService;
    private final IProjectPaperSizeService projectPaperSizeService;
    private final IProjectRegistrationDateService projectRegistrationDateService;
    private final IProjectNumberBookService projectNumberBookService;
    private final ILocationProvinceService locationProvinceService;
    private final ILocationDistrictService locationDistrictService;
    private final ILocationWardService locationWardService;
    private final IUserService userService;

    private final AppUtils appUtils;


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

    @GetMapping("/get-all-registration-type-by-ward/{wardId}")
    public ResponseEntity<ResponseObject> getAllRegistrationTypeByWard(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID phường / xã / thị trấn phải là một số") String wardId
    ) {
        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(wardId)
        ).orElseThrow(() -> {
            throw new DataInputException("Phường / Xã / Thị trấn không tồn tại");
        });

        List<ProjectRegistrationTypeResponse> registrationTypeResponses = projectRegistrationTypeService.findAllByProjectWard(
                projectWard
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách loại sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationTypeResponses)
                .build());
    }

    @GetMapping("/get-all-paper-size-by-registration-type/{registrationTypeId}")
    public ResponseEntity<ResponseObject> getAllPaperSizeByRegistrationType(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID loại sổ phải là một số") String registrationTypeId
    ) {
        ProjectRegistrationType projectRegistrationType = projectRegistrationTypeService.findById(
                Long.parseLong(registrationTypeId)
        ).orElseThrow(() -> {
            throw new DataInputException("Loại sổ không tồn tại");
        });

        List<ProjectPaperSizeResponse> paperSizeResponses = projectPaperSizeService.findAllByProjectRegistrationType(
                projectRegistrationType
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách kích thước sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(paperSizeResponses)
                .build());
    }

    @GetMapping("/get-all-registration-date-by-paper-size/{paperSizeId}")
    public ResponseEntity<ResponseObject> getAllRegistrationDateByPaperSize(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID kích thước sổ phải là một số") String paperSizeId
    ) {
        ProjectPaperSize projectPaperSize = projectPaperSizeService.findById(
                Long.parseLong(paperSizeId)
        ).orElseThrow(() -> {
            throw new DataInputException("Kích thước sổ không tồn tại");
        });

        List<ProjectRegistrationDateResponse> registrationDateResponses = projectRegistrationDateService.findAllByProjectPaperSize(
                projectPaperSize
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách năm đăng ký sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationDateResponses)
                .build());
    }

    @GetMapping("/get-all-number-book-by-registration-date/{registrationDateId}")
    public ResponseEntity<ResponseObject> getAllNumberBookByRegistrationDate(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID năm đăng ký phải là một số") String registrationDateId
    ) {
        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateService.findById(
                Long.parseLong(registrationDateId)
        ).orElseThrow(() -> {
            throw new DataInputException("Năm đăng ký sổ không tồn tại");
        });

        List<ProjectNumberBookResponse> numberBookResponses = projectNumberBookService.findAllByProjectRegistrationDate(
                projectRegistrationDate
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookResponses)
                .build());
    }

    @GetMapping("/get-total-count-extract-form-for-compare/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getTotalCountExtractFormForCompare(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID đợt phân phối phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        AccessPoint accessPoint = accessPointService.findById(
                Long.parseLong(accessPointId)
        ).orElseThrow(() -> {
            throw new DataInputException("Đợt phân phối không tồn tại");
        });

        TotalCountExtractFormResponse totalCountExtractFormResponse = projectService.getTotalCountExtractForm(project, accessPoint);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy tổng số lượng biểu mẫu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(totalCountExtractFormResponse)
                .build());
    }

    @GetMapping("/get-total-count-extract-form-new/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getTotalCountExtractFormNew(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID đợt phân phối phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        AccessPoint accessPoint = accessPointService.findById(
                Long.parseLong(accessPointId)
        ).orElseThrow(() -> {
            throw new DataInputException("Đợt phân phối không tồn tại");
        });

        TotalCountExtractFormNewResponse totalCountExtractFormNewResponse = projectService.getTotalCountExtractFormNew(project, accessPoint);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy tổng số lượng biểu mẫu chưa nhập thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(totalCountExtractFormNewResponse)
                .build());
    }

    @GetMapping("/get-all-extract-form-match-compared/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFormMatchCompared(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        List<ExtractFormMatchComparedResponse> extractFormMatchComparedResponses = projectService.findAllExtractFormMatchCompared(
                project,
                EInputStatus.MATCHING
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách biểu mẫu đã so sánh khớp thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFormMatchComparedResponses)
                .build());
    }

    @GetMapping("/get-all-extract-form-checked-matching/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFormCheckedMatching(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        List<ExtractFormCheckedMatchingResponse> extractFormCheckedMatchingResponses = projectService.findAllExtractFormCheckedMatching(
                project,
                EInputStatus.CHECKED_MATCHING
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách biểu mẫu đã kiểm tra so sánh khớp thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFormCheckedMatchingResponses)
                .build());
    }

    @GetMapping("/get-count-extract-form-not-assign/{projectId}")
    public ResponseEntity<ResponseObject> getCountExtractFormNotAssign(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        CountExtractFormNotAssignResponse countExtractFormNotAssignResponse = projectService.getCountExtractFormNotAssign(project);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy số lượng biểu mẫu chưa phân phối thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(countExtractFormNotAssignResponse)
                .build());
    }

    @GetMapping("/get-remaining-extract-form-final-matching/{projectId}/{registrationType}")
    public ResponseEntity<ResponseObject> getRemainingExtractFormFinalMatching(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @NotEmpty(message = "Loại tài liệu là bắt buộc") String registrationType
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        boolean isValidType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isValidType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        long remaining = projectService.getRemainingExtractFormFinalMatching(project, eRegistrationType);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy số lượng biểu mẫu còn lại để kết xuất dữ liệu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(remaining)
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

    @PostMapping("/verify-project-get-data-by-user")
    public ResponseEntity<ResponseObject> verifyProjectGetDataByUser(
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

        StatisticProjectResponse statisticProjectResponse = projectService.getStatisticById(
                project.getId()
        );

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("project", projectResponse);
        objectMap.put("statistic", statisticProjectResponse);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác thực dự án thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(objectMap)
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

    // Phân phối biểu mẫu cho từng người dùng theo từng loại biểu mẫu
    @PostMapping("/assign-extract-form-each-user-and-types/{projectId}")
    public ResponseEntity<ResponseObject> assignExtractFormEachUserAndType(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID Dự án phải là một số") String projectId,
            @Validated @RequestBody AssignExtractFormEachUserAndTypeDTO extractFormEachUserAndTypeDTO,
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

        List<ExtractFormCountTypeDTO> extractFormTypes = extractFormEachUserAndTypeDTO.getExtractFormTypes();

        if (extractFormTypes == null || extractFormTypes.isEmpty()) {
            throw new DataInputException("Danh sách phiếu không được để trống");
        }

        for (ExtractFormCountTypeDTO type : extractFormTypes) {
            if (!appUtils.isValidEvenNumber(type.getTotalCount())) {
                throw new DataInputException("Số lượng phiếu của mỗi loại phải là số chẵn lớn hơn 0");
            }

            boolean isValidType = ERegistrationType.checkValue(type.getRegistrationType());

            if (!isValidType) {
                throw new DataInputException("Danh sách loại sổ không hợp lệ");
            }
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("ID dự án không tồn tại");
        });

        User user = userService.findByUsername(
                extractFormEachUserAndTypeDTO.getUsername()
        ).orElseThrow(() -> {
            throw new DataInputException("Tài khoản người dùng không tồn tại");
        });

        projectUserService.findByProjectAndUser(project, user).orElseThrow(() -> {
            throw new DataInputException("Người dùng không thuộc dự án này");
        });

        projectService.assignExtractFormEachUserAndType(
                project,
                user,
                extractFormEachUserAndTypeDTO.getExtractFormTypes()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Gán phiếu nhập cho từng người dùng theo từng loại biểu mẫu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    // So sánh tự động các biểu mẫu trường ngắn và trường dài đã nhập
    @PostMapping("/auto-compare-extract-short-full/{accessPointId}")
    public ResponseEntity<ResponseObject> autoCompareExtractForm(
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

    // Kết xuất dữ liệu sang các bảng chính để export excel
    @PostMapping("/extract-data/{projectId}")
    public ResponseEntity<ResponseObject> extractData(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @Validated @RequestBody ExtractDataDTO extractDataDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi kết xuất dữ liệu")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (Long.parseLong(extractDataDTO.getCountExtract()) > 10000) {
            throw new DataInputException("Số lượng kết xuất tối đa mỗi lần là 10.000 biểu mẫu");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        boolean isValidType = ERegistrationType.checkValue(extractDataDTO.getRegistrationType().toUpperCase());

        if (!isValidType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(extractDataDTO.getRegistrationType().toUpperCase());

        projectService.extractData(project, eRegistrationType, Long.parseLong(extractDataDTO.getCountExtract()));

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Kết xuất dữ liệu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

}
