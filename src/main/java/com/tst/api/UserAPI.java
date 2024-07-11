package com.tst.api;

import com.tst.components.LocalizationUtils;
import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.user.UserChangeInfoDTO;
import com.tst.models.dtos.user.UserUpdateInfoDTO;
import com.tst.models.dtos.user.UserUpdatePasswordDTO;
import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.Project;
import com.tst.models.entities.Role;
import com.tst.models.entities.User;
import com.tst.models.responses.PaginationResponseObject;
import com.tst.models.responses.PagingResponseObject;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.user.UserAssignResponse;
import com.tst.models.responses.user.UserAssignWithRemainingTotalResponse;
import com.tst.models.responses.user.UserResponse;
import com.tst.services.accessPoint.IAccessPointService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectUser.IProjectUserService;
import com.tst.services.role.IRoleService;
import com.tst.services.user.IUserService;
import com.tst.services.userInfo.IUserInfoService;
import com.tst.utils.AppUtils;
import com.tst.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserAPI {

    private final LocalizationUtils localizationUtils;
    private final IRoleService roleService;
    private final IUserService userService;
    private final IUserInfoService userInfoService;
    private final IProjectService projectService;
    private final IProjectUserService projectUserService;
    private final IAccessPointService accessPointService;

    private final AppUtils appUtils;


    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllUser(
            @RequestParam(value = "q", defaultValue = "") String keyword,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit,
            @RequestParam(value = "_sort", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "_order", defaultValue = "desc") String order
    ) throws Exception {
        keyword = "%" + keyword + "%";
        Pageable pageable;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page - 1, limit, Sort.by(sortBy).descending());
        }

        Page<UserResponse> userResponses = userService.findAllUserResponse(keyword, pageable);

        if (userResponses.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.NO_CONTENT))
                            .status(HttpStatus.OK.value())
                            .statusText(HttpStatus.OK)
                            .build(),
                    HttpStatus.OK
            );
        } else {
            PagingResponseObject pagingResponseObject = new PagingResponseObject();
            pagingResponseObject.setCount(userResponses.getTotalElements());
            pagingResponseObject.setLimit(limit);
            pagingResponseObject.setTotalPages(userResponses.getTotalPages());
            pagingResponseObject.setOffset(page);

            PaginationResponseObject paginationResponseObject = new PaginationResponseObject();
            paginationResponseObject.setContent(userResponses.getContent());
            paginationResponseObject.setPaging(pagingResponseObject);

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Get user list successfully")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(paginationResponseObject)
                    .build());
        }
    }

    @GetMapping("/get-all-by-project/{projectId}")
    public ResponseEntity<ResponseObject> getAllUserByProject(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        List<UserAssignResponse> users = projectUserService.findAllUserAssignByProject(project);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get user list with project successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(users)
                .build());
    }

    @GetMapping("/get-all-with-remaining-total-by-project/{projectId}")
    public ResponseEntity<ResponseObject> getAllUserWithRemainingTotalByProject(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        List<UserAssignResponse> users = projectUserService.findAllUserAssignByProject(project);

        Long remainingTotal = projectService.getRemainingTotal(project);

        UserAssignWithRemainingTotalResponse userAssignWithRemainingTotal = new UserAssignWithRemainingTotalResponse()
                .setUsers(users)
                .setRemainingTotal(remainingTotal);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get user list with project successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(userAssignWithRemainingTotal)
                .build());
    }

    @GetMapping("/get-all-by-project-and-access-point/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getAllUserByProject(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        AccessPoint accessPoint = accessPointService.findById(
                Long.parseLong(accessPointId)
        ).orElseThrow(() -> new DataInputException("ID điểm truy cập không tồn tại"));

        List<UserAssignResponse> users = projectUserService.findAllByProjectAndAccessPoint(project, accessPoint);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách người nhập liệu theo mốc phân phối thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(users)
                .build());
    }

    @PatchMapping("/update-info")
    public ResponseEntity<ResponseObject> updateInfo(
            @Validated @RequestBody UserUpdateInfoDTO userUpdateInfoDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi cập nhật thông tin")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (!appUtils.isValidPhoneNumber(userUpdateInfoDTO.getPhoneNumber())) {
            throw new DataInputException("Số điện thoại không hợp ệ");
        }

        User user = userService.getAuthenticatedUser();

        userInfoService.updateInfo(user, userUpdateInfoDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Cập nhật thông tin thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/change-info/{userId}")
    public ResponseEntity<ResponseObject> changeInfo(
            @PathVariable @NotBlank(message = "ID người dùng là bắt buộc") String userId,
            @Validated @RequestBody UserChangeInfoDTO userChangeInfoDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi cập nhật thông tin")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (!appUtils.isValidPhoneNumber(userChangeInfoDTO.getPhoneNumber())) {
            throw new DataInputException("Số điện thoại không hợp ệ");
        }

        User currentUser = userService.getAuthenticatedUser();

        User userChange = userService.findById(
                userId
        ).orElseThrow(() -> new DataInputException("Người dùng không tồn tại"));

        if (currentUser.getId().equals(userChange.getId())) {
            throw new DataInputException("Bạn không thể thay đổi thông tin chính mình bằng chức năng này");
        }

        Role roleChange = roleService.findByCodeNumber(
                Integer.parseInt(userChangeInfoDTO.getRoleCode())
        ).orElseThrow(() -> new DataInputException("Mã vai trò không tồn tại"));

        Integer roleCodeChange = roleChange.getCodeNumber();
        Integer roleCode = currentUser.getRole().getCodeNumber();

        if (roleCode > roleCodeChange) {
            throw new PermissionDenyException("Bạn không đủ quyền hạn để cập nhật vai trò cấp cao");
        }

        userChange.setRole(roleChange);

        userInfoService.changeInfo(userChange, userChangeInfoDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Thay đổi thông tin người dùng thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword(
            @Validated @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi cập nhật mật khẩu")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        if (!userUpdatePasswordDTO.getPassword().equals(userUpdatePasswordDTO.getRetypePassword())) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi cập nhật mật khẩu")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        userService.changePassword(user, userUpdatePasswordDTO.getPassword());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Cập nhật mật khẩu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
