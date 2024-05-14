package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.extractFull.*;
import com.tst.models.dtos.project.ProjectExtractDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.*;
import com.tst.models.enums.EInputStatus;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.extractFull.*;
import com.tst.services.birthCertificateType.IBirthCertificateTypeService;
import com.tst.services.birthExtractFull.IBirthExtractFullService;
import com.tst.services.confirmationType.IConfirmationTypeService;
import com.tst.services.deathExtractFull.IDeathExtractFullService;
import com.tst.services.deathNoticeType.IDeathNoticeTypeService;
import com.tst.services.genderType.IGenderTypeService;
import com.tst.services.identificationType.IIdentificationTypeService;
import com.tst.services.intendedUseType.IIntendedUseTypeService;
import com.tst.services.maritalStatusType.IMaritalStatusTypeService;
import com.tst.services.marryExtractFull.IMarryExtractFullService;
import com.tst.services.parentsChildrenExtractFull.IParentsChildrenExtractFullService;
import com.tst.services.project.IProjectService;
import com.tst.services.projectUser.IProjectUserService;
import com.tst.services.registrationTypeDetail.IRegistrationTypeDetailService;
import com.tst.services.residenceType.IResidenceTypeService;
import com.tst.services.user.IUserService;
import com.tst.services.wedlockExtractFull.IWedlockExtractFullService;
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
@RequestMapping("${api.prefix}/extract-full")
@RequiredArgsConstructor
@Validated
public class ExtractFullAPI {

    private final IProjectService projectService;
    private final IProjectUserService projectUserService;
    private final IRegistrationTypeDetailService registrationTypeDetailService;
    private final IGenderTypeService genderTypeService;
    private final IBirthCertificateTypeService birthCertificateTypeService;
    private final IResidenceTypeService residenceTypeService;
    private final IIdentificationTypeService identificationTypeService;
    private final IConfirmationTypeService confirmationTypeService;
    private final IMaritalStatusTypeService maritalStatusTypeService;
    private final IIntendedUseTypeService intendedUseTypeService;
    private final IDeathNoticeTypeService deathNoticeTypeService;

    private final IParentsChildrenExtractFullService parentsChildrenExtractFullService;
    private final IBirthExtractFullService birthExtractFullService;
    private final IMarryExtractFullService marryExtractFullService;
    private final IWedlockExtractFullService wedlockExtractFullService;
    private final IDeathExtractFullService deathExtractFullService;
    private final IUserService userService;

    private final AppUtils appUtils;
    private final ModelMapper modelMapper;


    @GetMapping("/all/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "Dự án phải là một số") String projectId
    ) {
        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        List<ExtractFullResponse> extractFullResponses = projectService.findAllExtractFullResponse(project, user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường dài thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFullResponses)
                .build());
    }

    @GetMapping("/new/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFullNew(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "Dự án phải là một số") String projectId
    ) {
        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        List<ExtractFullResponse> extractFullResponses = projectService.findAllNewExtractFullResponse(project, user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường dài chưa nhập thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFullResponses)
                .build());
    }

    @GetMapping("/later/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFullLater(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "Dự án phải là một số") String projectId
    ) {
        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        List<ExtractFullResponse> extractFullResponses = projectService.findAllLaterExtractFullResponse(project, user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường dài xử lý sau thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFullResponses)
                .build());
    }

    @GetMapping("/imported/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFullImported(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "Dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        User importer = userService.getAuthenticatedUser();

        List<ExtractFullResponse> extractFullResponses = projectService.findAllImportedExtractFullResponse(project, importer);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường dài đã nhập thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFullResponses)
                .build());
    }

    @GetMapping("/parents-children/new/{id}")
    public ResponseEntity<ResponseObject> getParentsChildrenExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (parentsChildrenExtractFull.getImporter() == null || !parentsChildrenExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        ParentsChildrenExtractFullResponse parentsChildrenExtractFullResponse = modelMapper.map(
                parentsChildrenExtractFull,
                ParentsChildrenExtractFullResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.CMC);
//        parentsChildrenExtractFullResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<ConfirmationTypeResponse> confirmationTypeResponses = confirmationTypeService.findAllConfirmationTypeResponse();
//        parentsChildrenExtractFullResponse.setConfirmationType(confirmationTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        parentsChildrenExtractFullResponse.setParentResidenceType(residenceTypeResponses);
//        parentsChildrenExtractFullResponse.setChildResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        parentsChildrenExtractFullResponse.setParentIdentificationType(identificationTypeResponses);
//        parentsChildrenExtractFullResponse.setChildIdentificationType(identificationTypeResponses);
//        parentsChildrenExtractFullResponse.setPetitionerIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(parentsChildrenExtractFullResponse)
                .build());
    }

    @GetMapping("/birth/new/{id}")
    public ResponseEntity<ResponseObject> getBirthExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractFull.getImporter() == null || !birthExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        BirthExtractFullResponse birthExtractFullResponse = modelMapper.map(
                birthExtractFull,
                BirthExtractFullResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.KS);
//        birthExtractFullResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<GenderTypeResponse> genderTypeResponses = genderTypeService.findAllGenderTypeResponse();
//        birthExtractFullResponse.setBirtherGender(genderTypeResponses);
//
//        List<BirthCertificateTypeResponse> birthCertificateTypeResponses = birthCertificateTypeService.findAllBirthCertificateTypeResponse();
//        birthExtractFullResponse.setBirthCertificateType(birthCertificateTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        birthExtractFullResponse.setMomResidenceType(residenceTypeResponses);
//        birthExtractFullResponse.setDadResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        birthExtractFullResponse.setMomIdentificationType(identificationTypeResponses);
//        birthExtractFullResponse.setDadIdentificationType(identificationTypeResponses);
//        birthExtractFullResponse.setPetitionerIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(birthExtractFullResponse)
                .build());
    }

    @GetMapping("/marry/new/{id}")
    public ResponseEntity<ResponseObject> getMarryExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractFull.getImporter() == null || !marryExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        MarryExtractFullResponse marryExtractFullResponse = modelMapper.map(
                marryExtractFull,
                MarryExtractFullResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.KH);
//        marryExtractFullResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<MaritalStatusResponse> maritalStatusResponses = maritalStatusTypeService.findAllMaritalStatusResponse();
//        marryExtractFullResponse.setMaritalStatus(maritalStatusResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        marryExtractFullResponse.setHusbandResidenceType(residenceTypeResponses);
//        marryExtractFullResponse.setWifeResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        marryExtractFullResponse.setHusbandIdentificationType(identificationTypeResponses);
//        marryExtractFullResponse.setWifeIdentificationType(identificationTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(marryExtractFullResponse)
                .build());
    }

    @GetMapping("/wedlock/new/{id}")
    public ResponseEntity<ResponseObject> getWedlockExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractFull.getImporter() == null || !wedlockExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        WedlockExtractFullResponse wedlockExtractFullResponse = modelMapper.map(
                wedlockExtractFull,
                WedlockExtractFullResponse.class
        );

//        List<GenderTypeResponse> genderTypeResponses = genderTypeService.findAllGenderTypeResponse();
//        wedlockExtractFullResponse.setConfirmerGender(genderTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        wedlockExtractFullResponse.setConfirmerResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        wedlockExtractFullResponse.setConfirmerIdentificationType(identificationTypeResponses);
//        wedlockExtractFullResponse.setPetitionerIdentificationType(identificationTypeResponses);
//
//        List<IntendedUseTypeResponse> intendedUseTypeResponses = intendedUseTypeService.findAllIntendedUseTypeResponse();
//        wedlockExtractFullResponse.setConfirmerIntendedUseType(intendedUseTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(wedlockExtractFullResponse)
                .build());
    }

    @GetMapping("/death/new/{id}")
    public ResponseEntity<ResponseObject> getDeathExtractFull(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractFull.getImporter() == null || !deathExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        DeathExtractFullResponse deathExtractFullResponse = modelMapper.map(
                deathExtractFull,
                DeathExtractFullResponse.class
        );

//        List<RegistrationTypeDetailResponse> registrationTypeDetailResponses = registrationTypeDetailService.findAllRegistrationTypeDetailResponse(ERegistrationType.KT);
//        deathExtractFullResponse.setRegistrationType(registrationTypeDetailResponses);
//
//        List<GenderTypeResponse> genderTypeResponses = genderTypeService.findAllGenderTypeResponse();
//        deathExtractFullResponse.setDeadManGender(genderTypeResponses);
//
//        List<ResidenceTypeResponse> residenceTypeResponses = residenceTypeService.findAllResidenceTypeResponse();
//        deathExtractFullResponse.setDeadManResidenceType(residenceTypeResponses);
//
//        List<IdentificationTypeResponse> identificationTypeResponses = identificationTypeService.findAllIdentificationTypeResponse();
//        deathExtractFullResponse.setDeadManIdentificationType(identificationTypeResponses);
//        deathExtractFullResponse.setPetitionerIdentificationType(identificationTypeResponses);
//
//        List<DeathNoticeTypeResponse> deathNoticeTypeResponses = deathNoticeTypeService.findAllDeathNoticeTypeResponse();
//        deathExtractFullResponse.setDeathNoticeType(deathNoticeTypeResponses);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(deathExtractFullResponse)
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
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project, 
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (parentsChildrenExtractFull.getImporter() == null || !parentsChildrenExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        if (parentsChildrenExtractFull.getProject() != project) {
            throw new PermissionDenyException("Biểu mẫu không thuộc dự án này");
        }

        ParentsChildrenExtractFullResponse parentsChildrenExtractFullResponse = modelMapper.map(
                parentsChildrenExtractFull,
                ParentsChildrenExtractFullResponse.class
        );

        parentsChildrenExtractFullResponse.setFolderPath(parentsChildrenExtractFull.getProjectNumberBookFile().getFolderPath());
        parentsChildrenExtractFullResponse.setFileName(parentsChildrenExtractFull.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(parentsChildrenExtractFullResponse)
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
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractFull.getImporter() == null || !birthExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        if (birthExtractFull.getProject() != project) {
            throw new PermissionDenyException("Biểu mẫu không thuộc dự án này");
        }

        BirthExtractFullResponse birthExtractFullResponse = modelMapper.map(
                birthExtractFull,
                BirthExtractFullResponse.class
        );

        birthExtractFullResponse.setFolderPath(birthExtractFull.getProjectNumberBookFile().getFolderPath());
        birthExtractFullResponse.setFileName(birthExtractFull.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(birthExtractFullResponse)
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
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractFull.getImporter() == null || !marryExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        if (marryExtractFull.getProject() != project) {
            throw new PermissionDenyException("Biểu mẫu không thuộc dự án này");
        }

        MarryExtractFullResponse marryExtractFullResponse = modelMapper.map(
                marryExtractFull,
                MarryExtractFullResponse.class
        );

        marryExtractFullResponse.setFolderPath(marryExtractFull.getProjectNumberBookFile().getFolderPath());
        marryExtractFullResponse.setFileName(marryExtractFull.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(marryExtractFullResponse)
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
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractFull.getImporter() == null || !wedlockExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        if (wedlockExtractFull.getProject() != project) {
            throw new PermissionDenyException("Biểu mẫu không thuộc dự án này");
        }

        WedlockExtractFullResponse wedlockExtractFullResponse = modelMapper.map(
                wedlockExtractFull,
                WedlockExtractFullResponse.class
        );

        wedlockExtractFullResponse.setFolderPath(wedlockExtractFull.getProjectNumberBookFile().getFolderPath());
        wedlockExtractFullResponse.setFileName(wedlockExtractFull.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(wedlockExtractFullResponse)
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
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractFull.getImporter() == null || !deathExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        if (deathExtractFull.getProject() != project) {
            throw new PermissionDenyException("Biểu mẫu không thuộc dự án này");
        }

        DeathExtractFullResponse deathExtractFullResponse = modelMapper.map(
                deathExtractFull,
                DeathExtractFullResponse.class
        );

        deathExtractFullResponse.setFolderPath(deathExtractFull.getProjectNumberBookFile().getFolderPath());
        deathExtractFullResponse.setFileName(deathExtractFull.getProjectNumberBookFile().getFileName());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường dài của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(deathExtractFullResponse)
                .build());
    }

    @PatchMapping("/parents-children/new")
    public ResponseEntity<ResponseObject> importParentsChildrenExtractFullNew(
            @Validated @RequestBody ParentsChildrenExtractFullDTO parentsChildrenExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu cha mẹ con")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(parentsChildrenExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                Long.parseLong(parentsChildrenExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (parentsChildrenExtractFull.getImporter() == null || !parentsChildrenExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        parentsChildrenExtractFullService.importBeforeCompare(parentsChildrenExtractFull, parentsChildrenExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/birth/new")
    public ResponseEntity<ResponseObject> importBirthExtractFullNew(
            @Validated @RequestBody BirthExtractFullDTO birthExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu khai sinh")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(birthExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                Long.parseLong(birthExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractFull.getImporter() == null || !birthExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        birthExtractFullService.importBeforeCompare(birthExtractFull, birthExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/marry/new")
    public ResponseEntity<ResponseObject> importMarryExtractFullNew(
            @Validated @RequestBody MarryExtractFullDTO marryExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu kết hôn")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(marryExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                Long.parseLong(marryExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractFull.getImporter() == null || !marryExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        marryExtractFullService.importBeforeCompare(marryExtractFull, marryExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/wedlock/new")
    public ResponseEntity<ResponseObject> importWedlockExtractFullNew(
            @Validated @RequestBody WedlockExtractFullDTO wedlockExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu tình trạng hôn nhân")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(wedlockExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                Long.parseLong(wedlockExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractFull.getImporter() == null || !wedlockExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        wedlockExtractFullService.importBeforeCompare(wedlockExtractFull, wedlockExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/death/new")
    public ResponseEntity<ResponseObject> importDeathExtractFullNew(
            @Validated @RequestBody DeathExtractFullDTO deathExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu khai tử")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(deathExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                Long.parseLong(deathExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractFull.getImporter() == null || !deathExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        deathExtractFullService.importBeforeCompare(deathExtractFull, deathExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/parents-children/import")
    public ResponseEntity<ResponseObject> importParentsChildrenExtractFullBeforeCompare(
            @Validated @RequestBody ParentsChildrenExtractFullDTO parentsChildrenExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu cha mẹ con")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(parentsChildrenExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(parentsChildrenExtractFullDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });
        
        if (parentsChildrenExtractFull.getImporter() == null || !parentsChildrenExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        parentsChildrenExtractFullService.importBeforeCompare(parentsChildrenExtractFull, parentsChildrenExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/birth/import")
    public ResponseEntity<ResponseObject> importBirthExtractFullBeforeCompare(
            @Validated @RequestBody BirthExtractFullDTO birthExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu khai sinh")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(birthExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(birthExtractFullDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (birthExtractFull.getImporter() == null || !birthExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        birthExtractFullService.importBeforeCompare(birthExtractFull, birthExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/marry/import")
    public ResponseEntity<ResponseObject> importMarryExtractFullBeforeCompare(
            @Validated @RequestBody MarryExtractFullDTO marryExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu kết hôn")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(marryExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(marryExtractFullDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (marryExtractFull.getImporter() == null || !marryExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        marryExtractFullService.importBeforeCompare(marryExtractFull, marryExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/wedlock/import")
    public ResponseEntity<ResponseObject> importWedlockExtractFullBeforeCompare(
            @Validated @RequestBody WedlockExtractFullDTO wedlockExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu tình trạng hôn nhân")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(wedlockExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(wedlockExtractFullDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (wedlockExtractFull.getImporter() == null || !wedlockExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        wedlockExtractFullService.importBeforeCompare(wedlockExtractFull, wedlockExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/death/import")
    public ResponseEntity<ResponseObject> importDeathExtractFullBeforeCompare(
            @Validated @RequestBody DeathExtractFullDTO deathExtractFullDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường dài của biểu mẫu khai tử")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(deathExtractFullDTO.getProjectId())
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> {
            throw new PermissionDenyException("Bạn không thuộc dự án này");
        });

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatusBeforeCompare(
                Long.parseLong(deathExtractFullDTO.getId())
        ).orElseThrow(() -> {
            throw new DataInputException("ID biểu mẫu không tồn tại");
        });

        if (deathExtractFull.getImporter() == null || !deathExtractFull.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        deathExtractFullService.importBeforeCompare(deathExtractFull, deathExtractFullDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường dài của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

}
