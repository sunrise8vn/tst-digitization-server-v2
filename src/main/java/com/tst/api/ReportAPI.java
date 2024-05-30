package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.enums.EAccessPointStatus;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.report.*;
import com.tst.services.accessPoint.IAccessPointService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectUser.IProjectUserService;
import com.tst.services.user.IUserService;
import com.tst.utils.AppUtils;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/reports")
@RequiredArgsConstructor
@Validated
public class ReportAPI {

    private final IAccessPointService accessPointService;
    private final IProjectService projectService;
    private final IUserService userService;

    private final AppUtils appUtils;


    @GetMapping("/get-all-access-point-processing-of-user/{projectId}")
    public ResponseEntity<ResponseObject> getAllAccessPointProcessingOfUser(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
    ) {

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<AccessPointResponse> accessPointResponses = accessPointService.findAllAccessPointProcessingByProjectAndUserAndStatus(
                project,
                user,
                EAccessPointStatus.PROCESSING
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách đợt phân phối đang xử lý biểu mẫu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(accessPointResponses)
                .build());
    }

    @GetMapping("/importer-imported/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getAllImportedForImporterAndAccessPoint(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID mốc nhập liệu phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<ReportImporterImportedResponse> reportImporterImportedResponses = projectService.findAllImportedForImporter(
                project.getId(),
                Long.parseLong(accessPointId),
                user.getId()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy báo cáo cho người nhập liệu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(reportImporterImportedResponses)
                .build());
    }

    @GetMapping("/importer-compared/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getAllComparedForImporterAndAccessPoint(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID mốc nhập liệu phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<ReportImporterComparedResponse> reportImporterComparedResponses = projectService.findAllComparedForImporter(
                project.getId(),
                Long.parseLong(accessPointId),
                user.getId()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy báo cáo dữ liệu so sánh biểu mẫu cho người nhập liệu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(reportImporterComparedResponses)
                .build());
    }

    @GetMapping("/importer-checked/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getAllCheckedForImporterAndAccessPoint(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID mốc nhập liệu phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<ReportImporterCheckedResponse> reportImporterCheckedResponses = projectService.findAllCheckedForImporter(
                project.getId(),
                Long.parseLong(accessPointId),
                user.getId()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy báo cáo dữ liệu kiểm tra biểu mẫu đã so sánh cho người nhập liệu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(reportImporterCheckedResponses)
                .build());
    }

    @GetMapping("/importer-accepted/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getAllAcceptedForImporterAndAccessPoint(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID mốc nhập liệu phải là một số") String accessPointId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<ReportImporterAcceptedResponse> reportImporterAcceptedResponses = projectService.findAllAcceptedForImporter(
                project.getId(),
                Long.parseLong(accessPointId),
                user.getId()
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy báo cáo dữ liệu biểu mẫu được chấp nhận cho người nhập liệu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(reportImporterAcceptedResponses)
                .build());
    }
}
