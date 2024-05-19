package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.project.RegistrationNumberBookDTO;
import com.tst.models.entities.*;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.project.RegistrationNumberBookResponse;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.services.registrationType.IRegistrationTypeService;
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


@RestController
@RequestMapping("${api.prefix}/number-books")
@RequiredArgsConstructor
@Validated
public class NumberBookAPI {

    private final IRegistrationTypeService registrationTypeService;
    private final IProjectService projectService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectWardService projectWardService;
    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;

    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    @GetMapping("/get-all-by-project-and-province/{projectId}/{provinceId}")
    public ResponseEntity<ResponseObject> getAllByProjectAndProvince(
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

    @GetMapping("/get-all-by-project-and-district/{projectId}/{districtId}")
    public ResponseEntity<ResponseObject> getAllByProjectAndDistrict(
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

    @GetMapping("/get-all-by-project-and-ward/{projectId}/{wardId}")
    public ResponseEntity<ResponseObject> getAllByProjectAndWard(
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
