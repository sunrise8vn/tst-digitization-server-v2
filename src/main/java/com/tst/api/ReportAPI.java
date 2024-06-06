package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.enums.EAccessPointStatus;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse;
import com.tst.models.responses.report.*;
import com.tst.services.accessPoint.IAccessPointService;
import com.tst.services.accessPointHistory.IAccessPointHistoryService;
import com.tst.services.project.IProjectService;
import com.tst.services.user.IUserService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/reports")
@RequiredArgsConstructor
@Validated
public class ReportAPI {

    private final IAccessPointService accessPointService;
    private final IAccessPointHistoryService accessPointHistoryService;
    private final IProjectService projectService;
    private final IUserService userService;

    private final ModelMapper modelMapper;


    @GetMapping("/get-all-access-point-not-done-of-user/{projectId}")
    public ResponseEntity<ResponseObject> getAllAccessPointProcessingOfUser(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
    ) {

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<AccessPointResponse> accessPointResponses = accessPointService.findAllAccessPointProcessingByProjectAndUserAndStatusNot(
                project,
                user,
                EAccessPointStatus.DONE
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách đợt phân phối đang xử lý biểu mẫu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(accessPointResponses)
                .build());
    }

    @GetMapping("/get-all-access-point-not-done-for-manager/{projectId}")
    public ResponseEntity<ResponseObject> getAllAccessPointProcessingForManager(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
    ) {

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        List<AccessPointResponse> accessPointResponses = accessPointService.findAllAccessPointProcessingByProjectAndStatusNot(
                project,
                EAccessPointStatus.DONE
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách đợt phân phối đang xử lý biểu mẫu thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(accessPointResponses)
                .build());
    }

    @GetMapping("/importer-total/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getTotalExtractFormOfImporter(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId,
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

        User user = userService.getAuthenticatedUser();

        Optional<AccessPointHistory> accessPointHistoryOptional = accessPointHistoryService.findByProjectAndAccessPointAndAssignees(
                project,
                accessPoint,
                user
        );

        if (accessPointHistoryOptional.isEmpty()) {
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Không có dữ liệu biểu mẫu được phân phối")
                    .status(HttpStatus.NO_CONTENT.value())
                    .statusText(HttpStatus.NO_CONTENT)
                    .build());
        }

        ImporterAccessPointHistoryResponse importerAccessPointHistoryResponse = modelMapper.map(
                accessPointHistoryOptional.get(),
                ImporterAccessPointHistoryResponse.class
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu tổng số biểu mẫu được phân phối thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(importerAccessPointHistoryResponse)
                .build());
    }

    @GetMapping("/importer-all-total-not-done/{projectId}")
    public ResponseEntity<ResponseObject> getAllTotalExtractFormNotDoneOfImporter(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
    ) {

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User user = userService.getAuthenticatedUser();

        List<ImporterAccessPointHistoryResponse> accessPointHistoryResponses = accessPointHistoryService.findAllNotDoneByProjectAndAssignees(
          project,
          user
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu tổng số biểu mẫu được phân phối thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(accessPointHistoryResponses)
                .build());
    }

    @GetMapping("/get-all-total-not-done-for-manager/{projectId}")
    public ResponseEntity<ResponseObject> getAllTotalExtractFormNotDoneForManager(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
    ) {

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        List<TotalAccessPointHistoryResponse> accessPointHistoryResponses = accessPointHistoryService.findAllNotDoneByProject(
                project
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu tổng số biểu mẫu được phân phối thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(accessPointHistoryResponses)
                .build());
    }

    @GetMapping("/get-all-total-not-done-by-access-point-for-manager/{projectId}/{accessPointId}")
    public ResponseEntity<ResponseObject> getAllTotalExtractFormNotDoneByAccessPointForManager(
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

        List<TotalAccessPointHistoryResponse> accessPointHistoryResponses = accessPointHistoryService.findAllNotDoneByProjectAndAccessPoint(
                project,
                accessPoint
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu tổng số biểu mẫu được phân phối thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(accessPointHistoryResponses)
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
