package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.accessPoint.AccessPointRevokeDTO;
import com.tst.models.dtos.project.*;
import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.locationRegion.LocationResponse;
import com.tst.models.responses.project.ProjectResponse;
import com.tst.models.responses.project.RegistrationPointResponse;
import com.tst.services.accessPoint.IAccessPointService;
import com.tst.services.locationDistrict.ILocationDistrictService;
import com.tst.services.locationProvince.ILocationProvinceService;
import com.tst.services.locationWard.ILocationWardService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectUser.IProjectUserService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

}
