package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.extractFull.*;
import com.tst.models.dtos.project.ProjectExtractDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.*;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.enums.ETableName;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        List<ExtractFullResponse> extractFullResponses = projectService.findAllNewExtractFullResponse(project, user);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu danh sách trường dài chưa nhập thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(extractFullResponses)
                .build());
    }

    @GetMapping("/later-processing/{projectId}")
    public ResponseEntity<ResponseObject> getAllExtractFullLater(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "Dự án phải là một số") String projectId
    ) {
        User user = userService.getAuthenticatedUser();

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @GetMapping("/get-match-compared/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> getMatchCompared(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        User user = userService.getAuthenticatedUser();

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu cha mẹ con thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(parentsChildrenExtractFullResponse)
                        .build());
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu khai sinh thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(birthExtractFullResponse)
                        .build());
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu kết hôn thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(marryExtractFullResponse)
                        .build());
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu tình trạng hôn nhân thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(wedlockExtractFullResponse)
                        .build());
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu khai tử thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(deathExtractFullResponse)
                        .build());
            }
            default -> {
                return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Lấy dữ liệu trường dài đã so sánh không thành công")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .statusText(HttpStatus.BAD_REQUEST)
                        .build());
            }
        }
    }

    @GetMapping("/get-next-math-compared/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> getNextMathCompared(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        User user = userService.getAuthenticatedUser();

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu cha mẹ con thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(parentsChildrenExtractFullResponse)
                        .build());
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu khai sinh thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(birthExtractFullResponse)
                        .build());
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu kết hôn thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(marryExtractFullResponse)
                        .build());
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu tình trạng hôn nhân thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(wedlockExtractFullResponse)
                        .build());
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã so sánh của biểu mẫu khai tử thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(deathExtractFullResponse)
                        .build());
            }
            default -> {
                return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Lấy dữ liệu trường dài đã so sánh không thành công")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .statusText(HttpStatus.BAD_REQUEST)
                        .build());
            }
        }
    }

    @GetMapping("/get-checked-matching/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> getCheckedMatching(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        User user = userService.getAuthenticatedUser();

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu cha mẹ con thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(parentsChildrenExtractFullResponse)
                        .build());
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu khai sinh thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(birthExtractFullResponse)
                        .build());
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu kết hôn thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(marryExtractFullResponse)
                        .build());
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu tình trạng hôn nhân thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(wedlockExtractFullResponse)
                        .build());
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu khai tử thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(deathExtractFullResponse)
                        .build());
            }
            default -> {
                return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh không thành công")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .statusText(HttpStatus.BAD_REQUEST)
                        .build());
            }
        }
    }

    @GetMapping("/get-next-checked-matching/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> getNextCheckedMatching(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        User user = userService.getAuthenticatedUser();

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.CHECKED_MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu cha mẹ con thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(parentsChildrenExtractFullResponse)
                        .build());
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.CHECKED_MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu khai sinh thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(birthExtractFullResponse)
                        .build());
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.CHECKED_MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu kết hôn thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(marryExtractFullResponse)
                        .build());
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.CHECKED_MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu tình trạng hôn nhân thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(wedlockExtractFullResponse)
                        .build());
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findNextIdByStatusForChecked(
                        project,
                        EInputStatus.CHECKED_MATCHING,
                        Long.parseLong(id)
                ).orElseThrow(() -> new DataInputException("Không có biểu mẫu tiếp theo"));

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
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh của biểu mẫu khai tử thành công")
                        .status(HttpStatus.OK.value())
                        .statusText(HttpStatus.OK)
                        .data(deathExtractFullResponse)
                        .build());
            }
            default -> {
                return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Lấy dữ liệu trường dài đã kiểm tra so sánh không thành công")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .statusText(HttpStatus.BAD_REQUEST)
                        .build());
            }
        }
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project, 
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdForImporter(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @PostMapping("/get-next-parents-children")
    public ResponseEntity<ResponseObject> getNextParentsChildrenExtractBeforeCompare(
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
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        Optional<ParentsChildrenExtractFull> parentsChildrenExtractFull = parentsChildrenExtractFullService.findNextIdForImporter(
                project.getId(),
                user.getId(),
                Long.parseLong(projectExtractDTO.getId()),
                ETableName.ParentsChildrenExtractFull.getValue()
        );

        if (parentsChildrenExtractFull.isPresent()) {
            ParentsChildrenExtractFullResponse parentsChildrenExtractFullResponse = modelMapper.map(
                    parentsChildrenExtractFull,
                    ParentsChildrenExtractFullResponse.class
            );

            parentsChildrenExtractFullResponse.setFolderPath(parentsChildrenExtractFull.get().getProjectNumberBookFile().getFolderPath());
            parentsChildrenExtractFullResponse.setFileName(parentsChildrenExtractFull.get().getProjectNumberBookFile().getFileName());

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Lấy dữ liệu trường dài của biểu mẫu cha mẹ con thành công")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(parentsChildrenExtractFullResponse)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Không có biểu mẫu nào tiếp theo")
                .status(HttpStatus.NO_CONTENT.value())
                .statusText(HttpStatus.NO_CONTENT)
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdForImporter(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @PostMapping("/get-next-birth")
    public ResponseEntity<ResponseObject> getNextBirthExtractBeforeCompare(
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
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        Optional<BirthExtractFull> birthExtractFull = birthExtractFullService.findNextIdForImporter(
                project.getId(),
                user.getId(),
                Long.parseLong(projectExtractDTO.getId()),
                ETableName.BirthExtractFull.getValue()
        );

        if (birthExtractFull.isPresent()) {
            BirthExtractFullResponse birthExtractFullResponse = modelMapper.map(
                    birthExtractFull,
                    BirthExtractFullResponse.class
            );

            birthExtractFullResponse.setFolderPath(birthExtractFull.get().getProjectNumberBookFile().getFolderPath());
            birthExtractFullResponse.setFileName(birthExtractFull.get().getProjectNumberBookFile().getFileName());

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Lấy dữ liệu trường dài của biểu mẫu khai sinh thành công")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(birthExtractFullResponse)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Không có biểu mẫu nào tiếp theo")
                .status(HttpStatus.NO_CONTENT.value())
                .statusText(HttpStatus.NO_CONTENT)
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdForImporter(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @PostMapping("/get-next-marry")
    public ResponseEntity<ResponseObject> getNextMarryExtractBeforeCompare(
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
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        Optional<MarryExtractFull> marryExtractFull = marryExtractFullService.findNextIdForImporter(
                project.getId(),
                user.getId(),
                Long.parseLong(projectExtractDTO.getId()),
                ETableName.MarryExtractFull.getValue()
        );

        if (marryExtractFull.isPresent()) {
            MarryExtractFullResponse marryExtractFullResponse = modelMapper.map(
                    marryExtractFull,
                    MarryExtractFullResponse.class
            );

            marryExtractFullResponse.setFolderPath(marryExtractFull.get().getProjectNumberBookFile().getFolderPath());
            marryExtractFullResponse.setFileName(marryExtractFull.get().getProjectNumberBookFile().getFileName());

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Lấy dữ liệu trường dài của biểu mẫu kết hôn thành công")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(marryExtractFullResponse)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Không có biểu mẫu nào tiếp theo")
                .status(HttpStatus.NO_CONTENT.value())
                .statusText(HttpStatus.NO_CONTENT)
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdForImporter(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @PostMapping("/get-next-wedlock")
    public ResponseEntity<ResponseObject> getNextWedlockExtractBeforeCompare(
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
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        Optional<WedlockExtractFull> wedlockExtractFull = wedlockExtractFullService.findNextIdForImporter(
                project.getId(),
                user.getId(),
                Long.parseLong(projectExtractDTO.getId()),
                ETableName.WedlockExtractFull.getValue()
        );

        if (wedlockExtractFull.isPresent()) {
            WedlockExtractFullResponse wedlockExtractFullResponse = modelMapper.map(
                    wedlockExtractFull,
                    WedlockExtractFullResponse.class
            );

            wedlockExtractFullResponse.setFolderPath(wedlockExtractFull.get().getProjectNumberBookFile().getFolderPath());
            wedlockExtractFullResponse.setFileName(wedlockExtractFull.get().getProjectNumberBookFile().getFileName());

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Lấy dữ liệu trường dài của biểu mẫu tình trạng hôn nhân thành công")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(wedlockExtractFullResponse)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Không có biểu mẫu nào tiếp theo")
                .status(HttpStatus.NO_CONTENT.value())
                .statusText(HttpStatus.NO_CONTENT)
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdForImporter(
                Long.parseLong(projectExtractDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @PostMapping("/get-next-death")
    public ResponseEntity<ResponseObject> getNextDeathExtractBeforeCompare(
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
        ).orElseThrow(() -> new DataInputException("ID dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        Optional<DeathExtractFull> deathExtractFull = deathExtractFullService.findNextIdForImporter(
                project.getId(),
                user.getId(),
                Long.parseLong(projectExtractDTO.getId()),
                ETableName.DeathExtractFull.getValue()
        );

        if (deathExtractFull.isPresent()) {
            DeathExtractFullResponse deathExtractFullResponse = modelMapper.map(
                    deathExtractFull,
                    DeathExtractFullResponse.class
            );

            deathExtractFullResponse.setFolderPath(deathExtractFull.get().getProjectNumberBookFile().getFolderPath());
            deathExtractFullResponse.setFileName(deathExtractFull.get().getProjectNumberBookFile().getFileName());

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Lấy dữ liệu trường dài của biểu mẫu khai tử thành công")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(deathExtractFullResponse)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Không có biểu mẫu nào tiếp theo")
                .status(HttpStatus.NO_CONTENT.value())
                .statusText(HttpStatus.NO_CONTENT)
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                Long.parseLong(parentsChildrenExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                Long.parseLong(birthExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                Long.parseLong(marryExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                Long.parseLong(wedlockExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                Long.parseLong(deathExtractFullDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdForImporter(
                Long.parseLong(parentsChildrenExtractFullDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));
        
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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        BirthExtractFull birthExtractFull = birthExtractFullService.findByIdForImporter(
                Long.parseLong(birthExtractFullDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        MarryExtractFull marryExtractFull = marryExtractFullService.findByIdForImporter(
                Long.parseLong(marryExtractFullDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdForImporter(
                Long.parseLong(wedlockExtractFullDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        DeathExtractFull deathExtractFull = deathExtractFullService.findByIdForImporter(
                Long.parseLong(deathExtractFullDTO.getId())
        ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

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

    @PatchMapping("/verify-compared-match/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> verifyComparedMatch(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                parentsChildrenExtractFullService.verifyComparedMatch(parentsChildrenExtractFull, user);
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                birthExtractFullService.verifyComparedMatch(birthExtractFull, user);
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                marryExtractFullService.verifyComparedMatch(marryExtractFull, user);
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                wedlockExtractFullService.verifyComparedMatch(wedlockExtractFull, user);
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                deathExtractFullService.verifyComparedMatch(deathExtractFull, user);
            }
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác nhận kiểm tra biểu mẫu đúng thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/verify-compared-not-match/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> verifyComparedNotMatch(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                parentsChildrenExtractFullService.verifyComparedNotMatch(parentsChildrenExtractFull, user);
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                birthExtractFullService.verifyComparedNotMatch(birthExtractFull, user);
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                marryExtractFullService.verifyComparedNotMatch(marryExtractFull, user);
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                wedlockExtractFullService.verifyComparedNotMatch(wedlockExtractFull, user);
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                deathExtractFullService.verifyComparedNotMatch(deathExtractFull, user);
            }
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác nhận kiểm tra biểu mẫu sai thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/verify-checked-compared-match/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> verifyCheckedComparedMatch(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                parentsChildrenExtractFullService.verifyCheckedComparedMatch(parentsChildrenExtractFull, user);
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                birthExtractFullService.verifyCheckedComparedMatch(birthExtractFull, user);
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                marryExtractFullService.verifyCheckedComparedMatch(marryExtractFull, user);
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                wedlockExtractFullService.verifyCheckedComparedMatch(wedlockExtractFull, user);
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.CHECKED_MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                deathExtractFullService.verifyCheckedComparedMatch(deathExtractFull, user);
            }
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác nhận kiểm tra biểu mẫu đúng thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/verify-checked-compared-not-match/{registrationType}/{projectId}/{id}")
    public ResponseEntity<ResponseObject> verifyCheckedComparedNotMatch(
            @PathVariable @NotBlank(message = "Loại tài liệu là bắt buộc") String registrationType,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID dự án phải là một số") String projectId,
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        boolean isExistRegistrationType = ERegistrationType.checkValue(registrationType.toUpperCase());

        if (!isExistRegistrationType) {
            throw new DataInputException("Loại tài liệu không tồn tại");
        }

        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> new DataInputException("Dự án không tồn tại"));

        projectUserService.findByProjectAndUser(
                project,
                user
        ).orElseThrow(() -> new PermissionDenyException("Bạn không thuộc dự án này"));

        ERegistrationType eRegistrationType = ERegistrationType.valueOf(registrationType.toUpperCase());

        switch (eRegistrationType) {
            case CMC -> {
                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                parentsChildrenExtractFullService.verifyCheckedComparedNotMatch(parentsChildrenExtractFull, user);
            }
            case KS -> {
                BirthExtractFull birthExtractFull = birthExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                birthExtractFullService.verifyCheckedComparedNotMatch(birthExtractFull, user);
            }
            case KH -> {
                MarryExtractFull marryExtractFull = marryExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> {
                    throw new DataInputException("ID biểu mẫu không tồn tại");
                });

                marryExtractFullService.verifyCheckedComparedNotMatch(marryExtractFull, user);
            }
            case HN -> {
                WedlockExtractFull wedlockExtractFull = wedlockExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                wedlockExtractFullService.verifyCheckedComparedNotMatch(wedlockExtractFull, user);
            }
            case KT -> {
                DeathExtractFull deathExtractFull = deathExtractFullService.findByIdAndStatus(
                        Long.parseLong(id),
                        EInputStatus.MATCHING
                ).orElseThrow(() -> new DataInputException("ID biểu mẫu không tồn tại"));

                deathExtractFullService.verifyCheckedComparedNotMatch(deathExtractFull, user);
            }
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Xác nhận kiểm tra biểu mẫu sai thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

}
