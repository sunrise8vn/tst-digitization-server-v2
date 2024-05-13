package com.tst.api;

import com.tst.exceptions.DataNotFoundException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.extractShort.*;
import com.tst.models.dtos.project.ProjectExtractDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.extractFull.*;
import com.tst.models.responses.extractShort.*;
import com.tst.models.entities.User;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.typeList.*;
import com.tst.services.birthCertificateType.IBirthCertificateTypeService;
import com.tst.services.birthExtractShort.IBirthExtractShortService;
import com.tst.services.confirmationType.IConfirmationTypeService;
import com.tst.services.deathExtractShort.IDeathExtractShortService;
import com.tst.services.deathNoticeType.IDeathNoticeTypeService;
import com.tst.services.genderType.IGenderTypeService;
import com.tst.services.identificationType.IIdentificationTypeService;
import com.tst.services.maritalStatusType.IMaritalStatusTypeService;
import com.tst.services.marryExtractShort.IMarryExtractShortService;
import com.tst.services.parentsChildrenExtractShort.IParentsChildrenExtractShortService;
import com.tst.services.project.IProjectService;
import com.tst.services.registrationTypeDetail.IRegistrationTypeDetailService;
import com.tst.services.residenceType.IResidenceTypeService;
import com.tst.services.user.IUserService;
import com.tst.services.wedlockExtractShort.IWedlockExtractShortService;
import com.tst.utils.AppUtils;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/extract-short")
@RequiredArgsConstructor
@Validated
public class ExtractShortAPI {

    private final IProjectService projectService;
    private final IRegistrationTypeDetailService registrationTypeDetailService;
    private final IGenderTypeService genderTypeService;
    private final IBirthCertificateTypeService birthCertificateTypeService;
    private final IResidenceTypeService residenceTypeService;
    private final IIdentificationTypeService identificationTypeService;
    private final IConfirmationTypeService confirmationTypeService;
    private final IMaritalStatusTypeService maritalStatusTypeService;
    private final IDeathNoticeTypeService deathNoticeTypeService;

    private final IParentsChildrenExtractShortService parentsChildrenExtractShortService;
    private final IBirthExtractShortService birthExtractShortService;
    private final IMarryExtractShortService marryExtractShortService;
    private final IWedlockExtractShortService wedlockExtractShortService;
    private final IDeathExtractShortService deathExtractShortService;
    private final IUserService userService;

    private final AppUtils appUtils;
    private final ModelMapper modelMapper;


    @GetMapping("/all/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Dự án không tồn tại");
        });

        User importer = userService.getAuthenticatedUser();

        List<ExtractShortResponse> extractShortResponses = projectService.findAllExtractShortResponse(project, importer);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường ngắn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractShortResponses)
                .build());
    }

    @GetMapping("/new/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractShortNew(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Dự án không tồn tại");
        });

        User importer = userService.getAuthenticatedUser();

        List<ExtractShortResponse> extractShortResponses = projectService.findAllNewExtractShortResponse(project, importer);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường ngắn chưa nhập thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractShortResponses)
                .build());
    }

    @GetMapping("/later/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractShortLater(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Dự án không tồn tại");
        });

        User importer = userService.getAuthenticatedUser();

        List<ExtractShortResponse> extractShortResponses = projectService.findAllLaterExtractShortResponse(project, importer);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường ngắn xử lý sau thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractShortResponses)
                .build());
    }

    @GetMapping("/imported/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractShortImported(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Dự án không tồn tại");
        });

        User importer = userService.getAuthenticatedUser();

        List<ExtractShortResponse> extractShortResponses = projectService.findAllImportedExtractShortResponse(project, importer);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường ngắn đã nhập thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractShortResponses)
                .build());
    }

    @PostMapping("/get-parents-children")
    public ResponseEntity<ResponseObject> getParentsChildrenExtractBeforeCompare(
            @Validated @RequestBody ProjectExtractDTO projectExtractDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi lấy dữ liệu trường dài của biểu mẫu cha mẹ con")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectExtractDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID dự án không tồn tại");
        });

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortService.findByIdAndStatusBeforeCompare(
                project,
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (parentsChildrenExtractShort.getImporter() == null || !parentsChildrenExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        ParentsChildrenExtractShortResponse parentsChildrenExtractShortResponse = modelMapper.map(
                parentsChildrenExtractShort,
                ParentsChildrenExtractShortResponse.class
        );

        parentsChildrenExtractShortResponse.setFolderPath(parentsChildrenExtractShort.getProjectNumberBookFile().getFolderPath());
        parentsChildrenExtractShortResponse.setFileName(parentsChildrenExtractShort.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(parentsChildrenExtractShortResponse)
                .build());
    }

    @PostMapping("/get-birth")
    public ResponseEntity<ResponseObject> getBirthExtractBeforeCompare(
            @Validated @RequestBody ProjectExtractDTO projectExtractDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi lấy dữ liệu trường dài của biểu mẫu khai sinh")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectExtractDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID dự án không tồn tại");
        });

        BirthExtractShort birthExtractShort = birthExtractShortService.findByIdAndStatusBeforeCompare(
                project,
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractShort.getImporter() == null || !birthExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        BirthExtractShortResponse birthExtractShortResponse = modelMapper.map(
                birthExtractShort,
                BirthExtractShortResponse.class
        );

        birthExtractShortResponse.setFolderPath(birthExtractShort.getProjectNumberBookFile().getFolderPath());
        birthExtractShortResponse.setFileName(birthExtractShort.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(birthExtractShortResponse)
                .build());
    }

    @PostMapping("/get-marry")
    public ResponseEntity<ResponseObject> getMarryExtractBeforeCompare(
            @Validated @RequestBody ProjectExtractDTO projectExtractDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi lấy dữ liệu trường dài của biểu mẫu kết hôn")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectExtractDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID dự án không tồn tại");
        });

        MarryExtractShort marryExtractShort = marryExtractShortService.findByIdAndStatusBeforeCompare(
                project,
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractShort.getImporter() == null || !marryExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        MarryExtractShortResponse marryExtractShortResponse = modelMapper.map(
                marryExtractShort,
                MarryExtractShortResponse.class
        );

        marryExtractShortResponse.setFolderPath(marryExtractShort.getProjectNumberBookFile().getFolderPath());
        marryExtractShortResponse.setFileName(marryExtractShort.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(marryExtractShortResponse)
                .build());
    }

    @PostMapping("/get-wedlock")
    public ResponseEntity<ResponseObject> getWedlockExtractBeforeCompare(
            @Validated @RequestBody ProjectExtractDTO projectExtractDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi lấy dữ liệu trường dài của biểu mẫu tình trạng hôn nhân")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectExtractDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID dự án không tồn tại");
        });

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortService.findByIdAndStatusBeforeCompare(
                project,
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractShort.getImporter() == null || !wedlockExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        WedlockExtractShortResponse wedlockExtractShortResponse = modelMapper.map(
                wedlockExtractShort,
                WedlockExtractShortResponse.class
        );

        wedlockExtractShortResponse.setFolderPath(wedlockExtractShort.getProjectNumberBookFile().getFolderPath());
        wedlockExtractShortResponse.setFileName(wedlockExtractShort.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(wedlockExtractShortResponse)
                .build());
    }

    @PostMapping("/get-death")
    public ResponseEntity<ResponseObject> getDeathExtractBeforeCompare(
            @Validated @RequestBody ProjectExtractDTO projectExtractDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi lấy dữ liệu trường dài của biểu mẫu khai tử")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectExtractDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID dự án không tồn tại");
        });

        DeathExtractShort deathExtractShort = deathExtractShortService.findByIdAndStatusBeforeCompare(
                project,
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractShort.getImporter() == null || !deathExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        DeathExtractShortResponse deathExtractShortResponse = modelMapper.map(
                deathExtractShort,
                DeathExtractShortResponse.class
        );

        deathExtractShortResponse.setFolderPath(deathExtractShort.getProjectNumberBookFile().getFolderPath());
        deathExtractShortResponse.setFileName(deathExtractShort.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(deathExtractShortResponse)
                .build());
    }

    @GetMapping("/parents-children/new/{id}")
    public ResponseEntity<ResponseObject> getParentsChildrenExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (parentsChildrenExtractShort.getImporter() == null || !parentsChildrenExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        ParentsChildrenExtractShortResponse parentsChildrenExtractShortResponse = modelMapper.map(
                parentsChildrenExtractShort,
                ParentsChildrenExtractShortResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.CMC);
//        parentsChildrenExtractShortResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<ConfirmationTypeResponse> confirmationTypeResponses = confirmationTypeService.findAllConfirmationTypeResponse();
//        parentsChildrenExtractShortResponse.setConfirmationType(confirmationTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        parentsChildrenExtractShortResponse.setParentResidenceType(residenceTypeResponses);
//        parentsChildrenExtractShortResponse.setChildResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        parentsChildrenExtractShortResponse.setParentIdentificationType(identificationTypeResponses);
//        parentsChildrenExtractShortResponse.setChildIdentificationType(identificationTypeResponses);
//        parentsChildrenExtractShortResponse.setPetitionerIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(parentsChildrenExtractShortResponse)
                .build());
    }

    @GetMapping("/birth/new/{id}")
    public ResponseEntity<ResponseObject> getBirthExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        BirthExtractShort birthExtractShort = birthExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractShort.getImporter() == null || !birthExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        BirthExtractShortResponse birthExtractShortResponse = modelMapper.map(
                birthExtractShort,
                BirthExtractShortResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.KS);
//        birthExtractShortResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<GenderTypeResponse> genderTypeResponses = genderTypeService.findAllGenderTypeResponse();
//        birthExtractShortResponse.setBirtherGender(genderTypeResponses);
//
//        List<BirthCertificateTypeResponse> birthCertificateTypeResponses = birthCertificateTypeService.findAllBirthCertificateTypeResponse();
//        birthExtractShortResponse.setBirthCertificateType(birthCertificateTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        birthExtractShortResponse.setMomResidenceType(residenceTypeResponses);
//        birthExtractShortResponse.setDadResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        birthExtractShortResponse.setMomIdentificationType(identificationTypeResponses);
//        birthExtractShortResponse.setDadIdentificationType(identificationTypeResponses);
//        birthExtractShortResponse.setPetitionerIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(birthExtractShortResponse)
                .build());
    }

    @GetMapping("/marry/new/{id}")
    public ResponseEntity<ResponseObject> getMarryExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        MarryExtractShort marryExtractShort = marryExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractShort.getImporter() == null || !marryExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        MarryExtractShortResponse marryExtractShortResponse = modelMapper.map(
                marryExtractShort,
                MarryExtractShortResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.KH);
//        marryExtractShortResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<MaritalStatusResponse> maritalStatusResponses = maritalStatusTypeService.findAllMaritalStatusResponse();
//        marryExtractShortResponse.setMaritalStatus(maritalStatusResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        marryExtractShortResponse.setHusbandResidenceType(residenceTypeResponses);
//        marryExtractShortResponse.setWifeResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        marryExtractShortResponse.setHusbandIdentificationType(identificationTypeResponses);
//        marryExtractShortResponse.setWifeIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(marryExtractShortResponse)
                .build());
    }

    @GetMapping("/wedlock/new/{id}")
    public ResponseEntity<ResponseObject> getWedlockExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractShort.getImporter() == null || !wedlockExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        WedlockExtractShortResponse wedlockExtractShortResponse = modelMapper.map(
                wedlockExtractShort,
                WedlockExtractShortResponse.class
        );

//        List<GenderTypeResponse> genderTypeResponses = genderTypeService.findAllGenderTypeResponse();
//        wedlockExtractShortResponse.setConfirmerGender(genderTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        wedlockExtractShortResponse.setConfirmerResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        wedlockExtractShortResponse.setConfirmerIdentificationType(identificationTypeResponses);
//        wedlockExtractShortResponse.setPetitionerIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(wedlockExtractShortResponse)
                .build());
    }

    @GetMapping("/death/new/{id}")
    public ResponseEntity<ResponseObject> getDeathExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        DeathExtractShort deathExtractShort = deathExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractShort.getImporter() == null || !deathExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        DeathExtractShortResponse deathExtractShortResponse = modelMapper.map(
                deathExtractShort,
                DeathExtractShortResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.KT);
//        deathExtractShortResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<GenderTypeResponse> genderTypeResponses = genderTypeService.findAllGenderTypeResponse();
//        deathExtractShortResponse.setDeadManGender(genderTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        deathExtractShortResponse.setDeadManResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        deathExtractShortResponse.setDeadManIdentificationType(identificationTypeResponses);
//        deathExtractShortResponse.setPetitionerIdentificationType(identificationTypeResponses);
//
//        List<DeathNoticeTypeResponse> deathNoticeTypeResponses = deathNoticeTypeService.findAllDeathNoticeTypeResponse();
//        deathExtractShortResponse.setDeathNoticeType(deathNoticeTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(deathExtractShortResponse)
                .build());
    }

    @PatchMapping("/parents-children/new")
    public ResponseEntity<ResponseObject> importParentsChildrenExtractShort(
            @Validated @RequestBody ParentsChildrenExtractShortDTO parentsChildrenExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu cha mẹ con")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortService.findByIdAndStatus(
                Long.parseLong(parentsChildrenExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (parentsChildrenExtractShort.getImporter() == null || !parentsChildrenExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        parentsChildrenExtractShortService.update(parentsChildrenExtractShort, parentsChildrenExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/birth/new")
    public ResponseEntity<ResponseObject> importBirthExtractShort(
            @Validated @RequestBody BirthExtractShortDTO birthExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu khai sinh")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        BirthExtractShort birthExtractShort = birthExtractShortService.findByIdAndStatus(
                Long.parseLong(birthExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractShort.getImporter() == null || !birthExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        birthExtractShortService.update(birthExtractShort, birthExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/marry/new")
    public ResponseEntity<ResponseObject> importMarryExtractShort(
            @Validated @RequestBody MarryExtractShortDTO marryExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu kết hôn")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        MarryExtractShort marryExtractShort = marryExtractShortService.findByIdAndStatus(
                Long.parseLong(marryExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractShort.getImporter() == null || !marryExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        marryExtractShortService.update(marryExtractShort, marryExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/wedlock/new")
    public ResponseEntity<ResponseObject> importWedlockExtractShort(
            @Validated @RequestBody WedlockExtractShortDTO wedlockExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu tình trạng hôn nhân")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortService.findByIdAndStatus(
                Long.parseLong(wedlockExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractShort.getImporter() == null || !wedlockExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        wedlockExtractShortService.update(wedlockExtractShort, wedlockExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/death/new")
    public ResponseEntity<ResponseObject> importDeathExtractShort(
            @Validated @RequestBody DeathExtractShortDTO deathExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu khai tử")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        DeathExtractShort deathExtractShort = deathExtractShortService.findByIdAndStatus(
                Long.parseLong(deathExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractShort.getImporter() == null || !deathExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        deathExtractShortService.update(deathExtractShort, deathExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu tình khai tử")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
