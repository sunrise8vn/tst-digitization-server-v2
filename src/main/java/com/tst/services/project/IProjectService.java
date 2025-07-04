package com.tst.services.project;

import com.tst.models.dtos.project.ExtractFormCountTypeDTO;
import com.tst.models.dtos.project.ProjectCreateDTO;
import com.tst.models.dtos.project.ProjectUpdateDTO;
import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.extractFull.ExtractFullResponse;
import com.tst.models.responses.extractShort.ExtractShortResponse;
import com.tst.models.responses.locationRegion.LocationResponse;
import com.tst.models.responses.project.*;
import com.tst.models.responses.report.ReportImporterAcceptedResponse;
import com.tst.models.responses.report.ReportImporterCheckedResponse;
import com.tst.models.responses.report.ReportImporterComparedResponse;
import com.tst.models.responses.report.ReportImporterImportedResponse;
import com.tst.models.responses.statistic.StatisticProjectResponse;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProjectService extends IGeneralService<Project, Long> {

    Optional<ProjectByUserResponse> findProjectResponseByProjectAndUser(Project project, User user);

    TotalCountExtractFormResponse getTotalCountExtractForm(Project project, AccessPoint accessPoint);

    TotalCountExtractFormNewResponse getTotalCountExtractFormNew(Project project, AccessPoint accessPoint);

    CountExtractFormNotAssignResponse getCountExtractFormNotAssign(Project project);

    List<ProjectResponse> findAllProjectsResponse();

    List<ExtractShortResponse> findAllExtractShortResponse(Project project, User importer);

    List<ExtractFullResponse> findAllExtractFullResponse(Project project, User importer);

    List<ExtractShortResponse> findAllNewExtractShortResponse(Project project, User importer);

    List<ExtractFullResponse> findAllNewExtractFullResponse(Project project, User importer);

    List<ExtractShortResponse> findAllLaterExtractShortResponse(Project project, User importer);

    List<ExtractFullResponse> findAllLaterExtractFullResponse(Project project, User importer);

    List<ExtractShortResponse> findAllImportedExtractShortResponse(Project project, User importer);

    List<ExtractFullResponse> findAllImportedExtractFullResponse(Project project, User importer);

    List<RegistrationPointResponse> findAllRegistrationPointByProjectAndUser(Project project, User user);

    List<LocationResponse> findAllProvincesByProjectAndUser(Project project, User user);

    List<LocationResponse> findAllDistrictsByProjectAndProvinceAndUser(
            Project project,
            ProjectProvince projectProvince,
            User user
    );

    List<LocationResponse> findAllWardsByProjectAndDistrictAndUser(
            Project project,
            ProjectDistrict projectDistrict,
            User user
    );

    List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectProvince(
            Project project,
            ProjectProvince projectProvince
    );

    List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectDistrict(
            Project project,
            ProjectDistrict projectDistrict
    );

    List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectWard(
            Project project,
            ProjectWard projectWard
    );

    List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectProvince(
            Project project,
            ProjectProvince projectProvince
    );

    List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectDistrict(
            Project project,
            ProjectDistrict projectDistrict
    );

    List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectWard(
            Project project,
            ProjectWard projectWard
    );

    StatisticProjectResponse getStatisticById(
            Long id
    );

    Long getRemainingTotal(Project project);

    Long getRemainingExtractFormFinalMatching(Project project, ERegistrationType registrationType);

    Long getCountExtractForm(Project project, ERegistrationType registrationType);

    Long getCountAllByProjectWardAndNumberBook(ProjectWard projectWard, ERegistrationType registrationType, String numberBook);

    List<ReportImporterImportedResponse> findAllImportedForImporter(Long projectId, Long accessPointId, String userId);

    List<ReportImporterComparedResponse> findAllComparedForImporter(Long projectId, Long accessPointId, String userId);

    List<ReportImporterCheckedResponse> findAllCheckedForImporter(Long projectId, Long accessPointId, String userId);

    List<ReportImporterAcceptedResponse> findAllAcceptedForImporter(Long projectId, Long accessPointId, String userId);

    List<ExtractFormMatchComparedResponse> findAllExtractFormMatchCompared(Project project, EInputStatus status);

    List<ExtractFormCheckedMatchingResponse> findAllExtractFormCheckedMatching(Project project, EInputStatus status);

    void create(ProjectCreateDTO projectCreateDTO);

    void update(Project project, ProjectUpdateDTO projectUpdateDTO);

    void updatePaperCountSize(Project project);

    void createRegistrationPoint(
            Project project,
            LocationProvince locationProvince,
            LocationDistrict locationDistrict,
            LocationWard locationWard
    );

    void createRegistrationNumberBook(
            ProjectWard projectWard,
            RegistrationType registrationType,
            EPaperSize ePaperSize,
            String registrationDateCode,
            String numberBookCode,
            MultipartFile coverFile
    );

    void assignExtractFormToUser(Project project, Long totalCount, List<User> users);

    void assignExtractFormEachUserAndType(Project project, User user, List<ExtractFormCountTypeDTO> extractFormCountTypeDTOS);

    void  autoCompareExtractShortFull(AccessPoint accessPoint);

    void extractData(Project project, ERegistrationType registrationType, Long countExtract);

}
