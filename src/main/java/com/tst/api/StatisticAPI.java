package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.*;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.statistic.*;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookFile.IProjectNumberBookFileService;
import com.tst.services.projectPaperSize.IProjectPaperSizeService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectRegistrationDate.IProjectRegistrationDateService;
import com.tst.services.projectRegistrationType.IProjectRegistrationTypeService;
import com.tst.services.projectWard.IProjectWardService;
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
@RequestMapping("${api.prefix}/statistics")
@RequiredArgsConstructor
@Validated
public class StatisticAPI {

    private final IProjectService projectService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectWardService projectWardService;
    private final IProjectRegistrationTypeService projectRegistrationTypeService;
    private final IProjectPaperSizeService projectPaperSizeService;
    private final IProjectRegistrationDateService projectRegistrationDateService;
    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectNumberBookFileService projectNumberBookFileService;


    @GetMapping("/get-all-number-book-files/{numberBookId}")
    public ResponseEntity<ResponseObject> getAllNumberBookFileByNumberBook(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID số sổ phải là một số") String numberBookId
    ) {
        ProjectNumberBook projectNumberBook = projectNumberBookService.findById(
                Long.parseLong(numberBookId)
        ).orElseThrow(() -> new DataInputException("Số Sổ không tồn tại"));

        List<StatictisProjectNumberBookFileResponse> numberBookFileResponses = projectNumberBookFileService.findAllProjectNumberBookFileResponse(
                projectNumberBook
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách tập tin thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookFileResponses)
                .build());
    }

    @GetMapping("/get-all-number-books/{registrationDateId}")
    public ResponseEntity<ResponseObject> getAllNumberBookByRegistrationDate(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID năm đăng ký sổ phải là một số") String registrationDateId
    ) {
        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateService.findById(
                Long.parseLong(registrationDateId)
        ).orElseThrow(() -> new DataInputException("Năm đăng ký sổ không tồn tại"));

        List<StatisticProjectNumberBookResponse> numberBookResponses = projectNumberBookService.findAllStatisticByProjectRegistrationDate(
                projectRegistrationDate
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookResponses)
                .build());
    }

    @GetMapping("/get-all-registration-date/{paperSizeId}")
    public ResponseEntity<ResponseObject> getAllRegistrationDateByPaperSize(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID kích thước sổ phải là một số") String paperSizeId
    ) {
        ProjectPaperSize projectPaperSize = projectPaperSizeService.findById(
                Long.parseLong(paperSizeId)
        ).orElseThrow(() -> new DataInputException("Kích thước sổ không tồn tại"));

        List<StatisticProjectNumberBookResponse> numberBookResponses = projectRegistrationDateService.findAllStatisticByProjectPaperSize(
                projectPaperSize
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách năm đăng ký sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(numberBookResponses)
                .build());
    }

    @GetMapping("/get-all-paper-sizes/{registrationTypeId}")
    public ResponseEntity<ResponseObject> getAllPaperSizeByRegistrationType(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID loại sổ phải là một số") String registrationTypeId
    ) {
        ProjectRegistrationType projectRegistrationType = projectRegistrationTypeService.findById(
                Long.parseLong(registrationTypeId)
        ).orElseThrow(() -> new DataInputException("Loại sổ không tồn tại"));

        List<StatisticProjectPaperSizeResponse> projectPaperSizeResponses = projectPaperSizeService.findAllStatisticByProjectRegistrationType(
                projectRegistrationType
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách kích thước sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(projectPaperSizeResponses)
                .build());
    }

    @GetMapping("/get-all-registration-type/{projectWardId}")
    public ResponseEntity<ResponseObject> getAllRegistrationTypeByProjectWard(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Phường / Xã / Thị trấn phải là một số") String projectWardId
    ) {
        ProjectWard projectWard = projectWardService.findById(
                Long.parseLong(projectWardId)
        ).orElseThrow(() -> new DataInputException("Phường / Xã / Thị trấn không tồn tại"));

        List<StatisticProjectRegistrationTypeResponse> registrationTypeResponses = projectRegistrationTypeService.findAllStatisticByProjectWard(
                projectWard
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách loại sổ thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(registrationTypeResponses)
                .build());
    }

    @GetMapping("/get-all-project-ward/{projectDistrictId}")
    public ResponseEntity<ResponseObject> getAllProjectWardByProjectDistrict(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Quận / Huyện / Thành phố phải là một số") String projectDistrictId
    ) {
        ProjectDistrict projectDistrict = projectDistrictService.findById(
                Long.parseLong(projectDistrictId)
        ).orElseThrow(() -> new DataInputException("Quận / Huyện / Thành phố không tồn tại"));

        List<StatisticProjectWardResponse> projectWardResponses = projectWardService.findAllStatisticByProjectDistrict(
                projectDistrict
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách Phường / Xã / Thị trấn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(projectWardResponses)
                .build());
    }

    @GetMapping("/get-all-project-district/{projectProvinceId}")
    public ResponseEntity<ResponseObject> getAllProjectDistrictByProjectProvince(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Tỉnh / Thành phố phải là một số") String projectProvinceId
    ) {
        ProjectProvince projectProvince = projectProvinceService.findById(
                Long.parseLong(projectProvinceId)
        ).orElseThrow(() -> new DataInputException("Tỉnh / Thành phố không tồn tại"));

        List<StatisticProjectDistrictResponse> projectDistrictResponses = projectDistrictService.findAllStatisticByProjectProvince(
                projectProvince
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách Quận / Huyện / Thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(projectDistrictResponses)
                .build());
    }

    @GetMapping("/get-all-project-province/{projectId}")
    public ResponseEntity<ResponseObject> getAllProjectProvinceByProject(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        List<StatisticProjectProvinceResponse> projectProvinceResponses = projectProvinceService.findAllStatisticByProject(
                project
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy thống kê danh sách Tỉnh / Thành phố thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(projectProvinceResponses)
                .build());
    }

//    @GetMapping("/get-data-project/{projectId}")
//    public ResponseEntity<ResponseObject> getDataProjectById(
//            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID Dự án phải là một số") String projectId
//    ) {
//        Project project = projectService.findById(
//                Long.parseLong(projectId)
//        ).orElseThrow(() -> {
//            throw new DataInputException("Dự án không tồn tại");
//        });
//
//        StatisticProjectResponse projectResponse = projectService.getStatisticById(
//                project.getId()
//        );
//
//        return projectResponse.map(statisticProjectResponse -> ResponseEntity.ok().body(ResponseObject.builder()
//                .message("Lấy thống kê dự án thành công")
//                .status(HttpStatus.OK.value())
//                .statusText(HttpStatus.OK)
//                .data(statisticProjectResponse)
//                .build())).orElseGet(() -> ResponseEntity.ok().body(ResponseObject.builder()
//                .message("Lấy thống kê dự án thành công")
//                .status(HttpStatus.OK.value())
//                .statusText(HttpStatus.OK)
//                .build()));
//
//    }
}
