package com.tst.services.project;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.compare.*;
import com.tst.models.dtos.project.ExtractFormCountTypeDTO;
import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.*;
import com.tst.models.entities.extractFull.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.*;
import com.tst.models.responses.extractFull.ExtractFullResponse;
import com.tst.models.responses.extractShort.ExtractShortResponse;
import com.tst.models.responses.locationRegion.LocationResponse;
import com.tst.models.responses.project.*;
import com.tst.models.responses.report.ReportImporterAcceptedResponse;
import com.tst.models.responses.report.ReportImporterCheckedResponse;
import com.tst.models.responses.report.ReportImporterComparedResponse;
import com.tst.models.responses.report.ReportImporterImportedResponse;
import com.tst.models.responses.statistic.StatisticProjectResponse;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.*;
import com.tst.repositories.extractShort.*;
import com.tst.services.BatchService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProjectService implements IProjectService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;
    private final ProjectRepository projectRepository;
    private final ProjectProvinceRepository projectProvinceRepository;
    private final ProjectDistrictRepository projectDistrictRepository;
    private final ProjectWardRepository projectWardRepository;
    private final ProjectRegistrationTypeRepository projectRegistrationTypeRepository;
    private final ProjectPaperSizeRepository projectPaperSizeRepository;
    private final ProjectRegistrationDateRepository projectRegistrationDateRepository;
    private final ProjectNumberBookRepository projectNumberBookRepository;
    private final ProjectNumberBookCoverRepository projectNumberBookCoverRepository;

    private final ParentsChildrenExtractShortRepository parentsChildrenExtractShortRepository;
    private final ParentsChildrenExtractFullRepository parentsChildrenExtractFullRepository;
    private final BirthExtractShortRepository birthExtractShortRepository;
    private final BirthExtractFullRepository birthExtractFullRepository;
    private final MarryExtractShortRepository marryExtractShortRepository;
    private final MarryExtractFullRepository marryExtractFullRepository;
    private final WedlockExtractShortRepository wedlockExtractShortRepository;
    private final WedlockExtractFullRepository wedlockExtractFullRepository;
    private final DeathExtractShortRepository deathExtractShortRepository;
    private final DeathExtractFullRepository deathExtractFullRepository;

    private final IProjectNumberBookCoverService projectNumberBookCoverService;

    private final ModelMapper modelMapper;
    private final AppUtils appUtils;
    private final BatchService batchService;


    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public Optional<ProjectResponse> findProjectResponseByProjectAndUser(Project project, User user) {
        return projectRepository.findProjectResponseByProjectAndUser(project, user);
    }

    @Override
    public TotalCountExtractFormResponse getTotalCountExtractForm(Project project, AccessPoint accessPoint) {
        Long countAllParentsAndChildrenShortFormImported = parentsChildrenExtractShortRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllParentsAndChildrenFullFormImported = parentsChildrenExtractFullRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllBirthShortFormImported = birthExtractShortRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllBirthFullFormImported = birthExtractFullRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllMarryShortFormImported = marryExtractShortRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllMarryFullFormImported = marryExtractFullRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllWedlockShortFormImported = wedlockExtractShortRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllWedlockFullFormImported = wedlockExtractFullRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllDeathShortFormImported = deathExtractShortRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        Long countAllDeathFullFormImported = deathExtractFullRepository.countAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);

        Long totalExtractFormImported = countAllParentsAndChildrenShortFormImported +
                countAllParentsAndChildrenFullFormImported +
                countAllBirthShortFormImported +
                countAllBirthFullFormImported +
                countAllMarryShortFormImported +
                countAllMarryFullFormImported +
                countAllWedlockShortFormImported +
                countAllWedlockFullFormImported +
                countAllDeathShortFormImported +
                countAllDeathFullFormImported;

        Long totalExtractFormAssigned = accessPoint.getTotalCount();


        return new TotalCountExtractFormResponse()
                .setTotalExtractFormAssigned(totalExtractFormAssigned)
                .setTotalExtractFormImported(totalExtractFormImported);
    }

    @Override
    public TotalCountExtractFormNewResponse getTotalCountExtractFormNew(Project project, AccessPoint accessPoint) {
        Long countAllParentsAndChildrenShortFormNew = parentsChildrenExtractShortRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllParentsAndChildrenFullFormNew = parentsChildrenExtractFullRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllBirthShortFormNew = birthExtractShortRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllBirthFullFormNew = birthExtractFullRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllMarryShortFormNew = marryExtractShortRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllMarryFullFormNew = marryExtractFullRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllWedlockShortFormNew = wedlockExtractShortRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllWedlockFullFormNew = wedlockExtractFullRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllDeathShortFormNew = deathExtractShortRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);
        Long countAllDeathFullFormNew = deathExtractFullRepository.countAllByAccessPointAndStatus(accessPoint, EInputStatus.NEW);

        Long totalExtractFormNew = countAllParentsAndChildrenShortFormNew +
                countAllParentsAndChildrenFullFormNew +
                countAllBirthShortFormNew +
                countAllBirthFullFormNew +
                countAllMarryShortFormNew +
                countAllMarryFullFormNew +
                countAllWedlockShortFormNew +
                countAllWedlockFullFormNew +
                countAllDeathShortFormNew +
                countAllDeathFullFormNew;

        return new TotalCountExtractFormNewResponse()
                .setTotalExtractFormNew(totalExtractFormNew);
    }

    @Override
    public CountExtractFormNotAssignResponse getCountExtractFormNotAssign(Project project) {
        Long countAllParentsAndChildrenShort = parentsChildrenExtractShortRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllParentsAndChildrenFull = parentsChildrenExtractFullRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllBirthShort = birthExtractShortRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllBirthFull = birthExtractFullRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllMarryShort = marryExtractShortRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllMarryFull = marryExtractFullRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllWedlockShort = wedlockExtractShortRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllWedlockFull = wedlockExtractFullRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllDeathShort = deathExtractShortRepository.countAllByProjectAndAccessPointIsNull(project);
        Long countAllDeathFull = deathExtractFullRepository.countAllByProjectAndAccessPointIsNull(project);

        return new CountExtractFormNotAssignResponse()
                .setCountParentsChildrenShort(countAllParentsAndChildrenShort)
                .setCountParentsChildrenFull(countAllParentsAndChildrenFull)
                .setCountBirthShort(countAllBirthShort)
                .setCountBirthFull(countAllBirthFull)
                .setCountMarryShort(countAllMarryShort)
                .setCountMarryFull(countAllMarryFull)
                .setCountWedlockShort(countAllWedlockShort)
                .setCountWedlockFull(countAllWedlockFull)
                .setCountDeathShort(countAllDeathShort)
                .setCountDeathFull(countAllDeathFull);
    }

    @Override
    public List<ExtractShortResponse> findAllExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndImporter(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndImporter(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findAllByProjectAndImporter(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndImporter(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndImporter(project, importer);

        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        return extractShortResponses;
    }

    @Override
    public List<ExtractFullResponse> findAllExtractFullResponse(Project project, User importer) {
        List<ExtractFullResponse> extractFullResponses = new ArrayList<>();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporter(project, importer);

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<ExtractShortResponse> findAllNewExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterAndStatusNew(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        return extractShortResponses;
    }

    @Override
    public List<ExtractFullResponse> findAllNewExtractFullResponse(Project project, User importer) {
        List<ExtractFullResponse> extractFullResponses = new ArrayList<>();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporterAndStatusNew(project, importer);

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<ExtractShortResponse> findAllLaterExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterAndStatusLater(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndImporterAndStatusLater(project, importer);


        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        return extractShortResponses;
    }

    @Override
    public List<ExtractFullResponse> findAllLaterExtractFullResponse(Project project, User importer) {
        List<ExtractFullResponse> extractFullResponses = new ArrayList<>();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporterAndStatusLater(project, importer);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporterAndStatusLater(project, importer);


        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<ExtractShortResponse> findAllImportedExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterAndStatusImported(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndImporterAndStatusImported(project, importer);


        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        return extractShortResponses;
    }

    @Override
    public List<ExtractFullResponse> findAllImportedExtractFullResponse(Project project, User importer) {
        List<ExtractFullResponse> extractFullResponses = new ArrayList<>();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporterAndStatusImported(project, importer);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporterAndStatusImported(project, importer);


        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            String checkedStatus = item.getStatus() == EInputStatus.IMPORTED
                    ? ""
                    : item.getStatus() == EInputStatus.MATCHING
                    ? "Đúng"
                    : "Sai";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCompareCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<RegistrationPointResponse> findAllRegistrationPointByProjectAndUser(Project project, User user) {
        return projectRepository.findAllRegistrationPointByProjectAndUser(project, user);
    }

    @Override
    public List<LocationResponse> findAllProvincesByProjectAndUser(Project project, User user) {
        return projectRepository.findAllProvincesByProjectAndUser(project, user);
    }

    @Override
    public List<LocationResponse> findAllDistrictsByProjectAndProvinceAndUser(Project project, ProjectProvince projectProvince, User user) {
        return projectRepository.findAllDistrictsByProjectAndProvinceAndUser(project, projectProvince, user);
    }

    @Override
    public List<LocationResponse> findAllWardsByProjectAndDistrictAndUser(Project project, ProjectDistrict projectDistrict, User user) {
        return projectRepository.findAllWardsByProjectAndDistrictAndUser(project, projectDistrict, user);
    }

    @Override
    public List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectProvince(Project project, ProjectProvince projectProvince) {
        return projectRepository.findAllNewNumberBooksByProjectAndProjectProvince(project, projectProvince);
    }

    @Override
    public List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectDistrict(Project project, ProjectDistrict projectDistrict) {
        return projectRepository.findAllNewNumberBooksByProjectAndProjectDistrict(project, projectDistrict);
    }

    @Override
    public List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectWard(Project project, ProjectWard projectWard) {
        return projectRepository.findAllNewNumberBooksByProjectAndProjectWard(project, projectWard);
    }

    @Override
    public List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectProvince(Project project, ProjectProvince projectProvince) {
        return projectRepository.findAllApprovedNumberBooksByProjectAndProjectProvince(project, projectProvince);
    }

    @Override
    public List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectDistrict(Project project, ProjectDistrict projectDistrict) {
        return projectRepository.findAllApprovedNumberBooksByProjectAndProjectDistrict(project, projectDistrict);
    }

    @Override
    public List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectWard(Project project, ProjectWard projectWard) {
        return projectRepository.findAllApprovedNumberBooksByProjectAndProjectWard(project, projectWard);
    }

    @Override
    public StatisticProjectResponse getStatisticById(Long id) {
        return projectRepository.getStatisticById(id);
    }

    @Override
    public Long getRemainingTotal(Project project) {
        long totalCountExtractShort = 0L;
        long totalCountExtractFull = 0L;

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += parentsChildrenExtractShorts.size();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += parentsChildrenExtractFulls.size();

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += birthExtractShorts.size();

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += birthExtractFulls.size();

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += marryExtractShorts.size();

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += marryExtractFulls.size();

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += wedlockExtractShorts.size();

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += wedlockExtractFulls.size();

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += deathExtractShorts.size();

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += deathExtractFulls.size();

        return totalCountExtractShort + totalCountExtractFull;
    }

    @Override
    @Transactional
    public List<ReportImporterImportedResponse> findAllImportedForImporter(Long projectId, Long accessPointId, String userId) {
        List<Object[]> results = projectRepository.findAllImportedForImporter(projectId, accessPointId, userId);

        return results.stream()
                .map(row -> {
                    ReportImporterImportedResponse response = new ReportImporterImportedResponse();
                    response.setImportedAt(((Date) row[0]).toLocalDate());
                    response.setFullLater(((BigDecimal) row[1]).toBigInteger().longValue());
                    response.setShortLater(((BigDecimal) row[2]).toBigInteger().longValue());
                    response.setFullImported(((BigDecimal) row[3]).toBigInteger().longValue());
                    response.setShortImported(((BigDecimal) row[4]).toBigInteger().longValue());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportImporterComparedResponse> findAllComparedForImporter(Long projectId, Long accessPointId, String userId) {
        List<Object[]> results = projectRepository.findAllComparedForImporter(projectId, accessPointId, userId);

        return results.stream()
                .map(row -> {
                    ReportImporterComparedResponse response = new ReportImporterComparedResponse();
                    response.setImportedAt(((Date) row[0]).toLocalDate());
                    response.setFullNotMatch(((BigDecimal) row[1]).toBigInteger().longValue());
                    response.setShortNotMatch(((BigDecimal) row[2]).toBigInteger().longValue());
                    response.setFullMatch(((BigDecimal) row[3]).toBigInteger().longValue());
                    response.setShortMatch(((BigDecimal) row[4]).toBigInteger().longValue());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportImporterCheckedResponse> findAllCheckedForImporter(Long projectId, Long accessPointId, String userId) {
        List<Object[]> results = projectRepository.findAllCheckedForImporter(projectId, accessPointId, userId);

        return results.stream()
                .map(row -> {
                    ReportImporterCheckedResponse response = new ReportImporterCheckedResponse();
                    response.setCheckedAt(((Date) row[0]).toLocalDate());
                    response.setFullCheckedMatch(((BigDecimal) row[1]).toBigInteger().longValue());
                    response.setShortCheckedMatch(((BigDecimal) row[2]).toBigInteger().longValue());
                    response.setFullCheckedNotMatch(((BigDecimal) row[3]).toBigInteger().longValue());
                    response.setShortCheckedNotMatch(((BigDecimal) row[4]).toBigInteger().longValue());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportImporterAcceptedResponse> findAllAcceptedForImporter(Long projectId, Long accessPointId, String userId) {
        List<Object[]> results = projectRepository.findAllAcceptedForImporter(projectId, accessPointId, userId);

        return results.stream()
                .map(row -> {
                    ReportImporterAcceptedResponse response = new ReportImporterAcceptedResponse();
                    response.setAcceptedAt(((Date) row[0]).toLocalDate());
                    response.setFullAccepted(((BigDecimal) row[1]).toBigInteger().longValue());
                    response.setShortAccepted(((BigDecimal) row[2]).toBigInteger().longValue());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ExtractFormMatchComparedResponse> findAllExtractFormMatchCompared(Project project, EInputStatus status) {
        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<ExtractFormMatchComparedResponse> extractFormMatchComparedResponses = new ArrayList<>();

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            ExtractFormMatchComparedResponse extractFormMatchComparedResponse = new ExtractFormMatchComparedResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormMatchComparedResponses.add(extractFormMatchComparedResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            ExtractFormMatchComparedResponse extractFormMatchComparedResponse = new ExtractFormMatchComparedResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormMatchComparedResponses.add(extractFormMatchComparedResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            ExtractFormMatchComparedResponse extractFormMatchComparedResponse = new ExtractFormMatchComparedResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormMatchComparedResponses.add(extractFormMatchComparedResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            ExtractFormMatchComparedResponse extractFormMatchComparedResponse = new ExtractFormMatchComparedResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormMatchComparedResponses.add(extractFormMatchComparedResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            ExtractFormMatchComparedResponse extractFormMatchComparedResponse = new ExtractFormMatchComparedResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormMatchComparedResponses.add(extractFormMatchComparedResponse);
        }

        return extractFormMatchComparedResponses;
    }

    @Override
    public List<ExtractFormCheckedMatchingResponse> findAllExtractFormCheckedMatching(Project project, EInputStatus status) {
        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndStatus(project, status);

        List<ExtractFormCheckedMatchingResponse> extractFormCheckedMatchingResponses = new ArrayList<>();

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            ExtractFormCheckedMatchingResponse extractFormCheckedMatchingResponse = new ExtractFormCheckedMatchingResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormCheckedMatchingResponses.add(extractFormCheckedMatchingResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            ExtractFormCheckedMatchingResponse extractFormCheckedMatchingResponse = new ExtractFormCheckedMatchingResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormCheckedMatchingResponses.add(extractFormCheckedMatchingResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            ExtractFormCheckedMatchingResponse extractFormCheckedMatchingResponse = new ExtractFormCheckedMatchingResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormCheckedMatchingResponses.add(extractFormCheckedMatchingResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            ExtractFormCheckedMatchingResponse extractFormCheckedMatchingResponse = new ExtractFormCheckedMatchingResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormCheckedMatchingResponses.add(extractFormCheckedMatchingResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            ExtractFormCheckedMatchingResponse extractFormCheckedMatchingResponse = new ExtractFormCheckedMatchingResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setImportedAt(item.getImportedAt());

            extractFormCheckedMatchingResponses.add(extractFormCheckedMatchingResponse);
        }

        return extractFormCheckedMatchingResponses;
    }

    @Override
    @Transactional
    public void createRegistrationPoint(
            Project project,
            LocationProvince locationProvince,
            LocationDistrict locationDistrict,
            LocationWard locationWard
    ) {
        Optional<ProjectProvince> projectProvinceOptional = projectProvinceRepository.findByProjectAndCode(
                project, locationProvince.getCode()
        );

        ProjectProvince projectProvince;

        if (projectProvinceOptional.isEmpty()) {
            projectProvince = new ProjectProvince()
                    .setProject(project)
                    .setCode(locationProvince.getCode())
                    .setName(locationProvince.getName());
            projectProvinceRepository.save(projectProvince);
        }
        else {
            projectProvince = projectProvinceOptional.get();
        }

        Optional<ProjectDistrict> projectDistrictOptional = projectDistrictRepository.findByProjectAndProjectProvinceAndCode(
                project,
                projectProvince,
                locationDistrict.getCode()
        );

        ProjectDistrict projectDistrict;

        if (projectDistrictOptional.isEmpty()) {
            projectDistrict = new ProjectDistrict()
                    .setProject(project)
                    .setProjectProvince(projectProvince)
                    .setName(locationDistrict.getName())
                    .setCode(locationDistrict.getCode());
            projectDistrictRepository.save(projectDistrict);
        }
        else {
            projectDistrict = projectDistrictOptional.get();
        }

        Optional<ProjectWard> projectWardOptional = projectWardRepository.findByProjectAndProjectDistrictAndCode(
                project,
                projectDistrict,
                locationWard.getCode()
        );

        if (projectWardOptional.isPresent()) {
            throw new DataInputException("Phường / Xã / Thị trấn này đã được đăng ký trong dự án này");
        }

        ProjectWard projectWard = new ProjectWard()
                .setProject(project)
                .setProjectDistrict(projectDistrict)
                .setName(locationWard.getName())
                .setCode(locationWard.getCode());
        projectWardRepository.save(projectWard);
    }

    @Override
    @Transactional
    public void createRegistrationNumberBook(
            ProjectWard projectWard,
            RegistrationType registrationType,
            EPaperSize ePaperSize,
            String registrationDateCode,
            String numberBookCode,
            MultipartFile coverFile
    ) {
        Optional<ProjectRegistrationType> projectRegistrationTypeOptional = projectRegistrationTypeRepository.findByProjectWardAndCode(
                projectWard,
                registrationType.getCode());

        ProjectRegistrationType projectRegistrationType;

        if (projectRegistrationTypeOptional.isEmpty()) {
            projectRegistrationType = new ProjectRegistrationType()
                    .setProject(projectWard.getProject())
                    .setProjectWard(projectWard)
                    .setCode(registrationType.getCode());
            projectRegistrationTypeRepository.save(projectRegistrationType);
        }
        else {
            projectRegistrationType = projectRegistrationTypeOptional.get();
        }

        Optional<ProjectPaperSize> projectPaperSizeOptional = projectPaperSizeRepository.findByProjectRegistrationTypeAndCode(
                projectRegistrationType,
                ePaperSize
        );

        ProjectPaperSize projectPaperSize;

        if (projectPaperSizeOptional.isEmpty()) {
            projectPaperSize = new ProjectPaperSize()
                    .setProject(projectWard.getProject())
                    .setProjectRegistrationType(projectRegistrationType)
                    .setCode(ePaperSize);
            projectPaperSizeRepository.save(projectPaperSize);
        }
        else {
            projectPaperSize = projectPaperSizeOptional.get();
        }

        Optional<ProjectRegistrationDate> projectRegistrationDateOptional = projectRegistrationDateRepository.findByProjectPaperSizeAndCode(
                projectPaperSize,
                registrationDateCode
        );

        ProjectRegistrationDate projectRegistrationDate;

        if (projectRegistrationDateOptional.isEmpty()) {
            projectRegistrationDate = new ProjectRegistrationDate()
                    .setProject(projectWard.getProject())
                    .setProjectPaperSize(projectPaperSize)
                    .setCode(registrationDateCode);
            projectRegistrationDateRepository.save(projectRegistrationDate);
        }
        else {
            projectRegistrationDate = projectRegistrationDateOptional.get();
        }

        Optional<ProjectNumberBook> projectNumberBookOptional = projectNumberBookRepository.findByProjectRegistrationDateAndCodeAndStatusNot(
                projectRegistrationDate,
                numberBookCode,
                EProjectNumberBookStatus.CANCEL
        );

        if (projectNumberBookOptional.isPresent()) {
            String dateCode = projectRegistrationDate.getCode();
            String pageSizeCode = projectPaperSize.getCode().getValue();

            if (projectNumberBookOptional.get().getStatus().equals(EProjectNumberBookStatus.NEW)) {
                throw new DataInputException("Quyển sổ " + numberBookCode + " năm " + dateCode + " khổ giấy " + pageSizeCode + " đang chờ xét duyệt");
            }

            if (projectNumberBookOptional.get().getStatus().equals(EProjectNumberBookStatus.ACCEPT)) {
                throw new DataInputException("Quyển sổ " + numberBookCode + " đã được sử dụng trong năm " + dateCode);
            }
        }
        else {
            this.uploadCoverFileAndSave(
                    projectWard,
                    projectRegistrationType,
                    projectPaperSize,
                    projectRegistrationDate,
                    numberBookCode,
                    coverFile
            );
        }
    }

    public void uploadCoverFileAndSave(
            ProjectWard projectWard,
            ProjectRegistrationType projectRegistrationType,
            ProjectPaperSize projectPaperSize,
            ProjectRegistrationDate projectRegistrationDate,
            String numberBookCode,
            MultipartFile coverFile
    ) {
        ProjectDistrict projectDistrict = projectWard.getProjectDistrict();
        ProjectProvince projectProvince = projectDistrict.getProjectProvince();
        Project project = projectProvince.getProject();

        String coverFileName = "Cover." +
                projectWard.getCode() + "." +
                projectRegistrationType.getCode() + "." +
                projectPaperSize.getCode() + "." +
                projectRegistrationDate.getCode() + "." +
                numberBookCode +
                ".pdf";

        String coverFolderPath = project.getFolder() + "/" +
                projectProvince.getCode() + "/" +
                projectDistrict.getCode() + "/" +
                projectWard.getCode() + "/" +
                projectRegistrationType.getCode() + "/" +
                projectPaperSize.getCode() + "/" +
                projectRegistrationDate.getCode() + "/" +
                numberBookCode;

        Long fileSize = coverFile.getSize() / 1024; // KB

        projectNumberBookCoverService.uploadCoverFile(coverFile, coverFileName, coverFolderPath);

        ProjectNumberBookCover projectNumberBookCover = new ProjectNumberBookCover()
                .setProject(projectWard.getProject())
                .setFileName(coverFileName)
                .setFolderPath(coverFolderPath)
                .setFileSize(fileSize);
        projectNumberBookCoverRepository.save(projectNumberBookCover);

        ProjectNumberBook projectNumberBook = new ProjectNumberBook()
                .setProject(projectWard.getProject())
                .setProjectRegistrationDate(projectRegistrationDate)
                .setProjectNumberBookCover(projectNumberBookCover)
                .setCode(numberBookCode)
                .setStatus(EProjectNumberBookStatus.NEW);
        projectNumberBookRepository.save(projectNumberBook);
    }

    @Override
    public void updatePaperCountSize(Project project) {
        PaperSizeDTO paperSizeDTO = projectProvinceRepository.findByProject(project);

        project.setA0(paperSizeDTO.getA0());
        project.setA1(paperSizeDTO.getA1());
        project.setA2(paperSizeDTO.getA2());
        project.setA3(paperSizeDTO.getA3());
        project.setA4(paperSizeDTO.getA4());
        project.setConvertA4(paperSizeDTO.getConvertA4());
        project.setTotalSize(paperSizeDTO.getTotalSize());

        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void assignExtractFormToUser(Project project, Long totalCount, List<User> users) {
        long totalCountExtractShort = 0L;
        long totalCountExtractFull = 0L;

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += parentsChildrenExtractShorts.size();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += parentsChildrenExtractFulls.size();

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += birthExtractShorts.size();

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += birthExtractFulls.size();

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += marryExtractShorts.size();

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += marryExtractFulls.size();

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += wedlockExtractShorts.size();

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += wedlockExtractFulls.size();

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractShort += deathExtractShorts.size();

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += deathExtractFulls.size();

        long totalCountExtractForm = totalCountExtractShort + totalCountExtractFull;

        if (totalCount > totalCountExtractForm) {
            throw new DataInputException("Tổng số phiếu cần nhập chỉ còn " + totalCountExtractForm + " phiếu");
        }

        long countExtractShort = 0L;
        long countExtractFull = 0L;

        //  Khởi tạo danh sách các đối tượng đã cập nhật để lưu vào db
        List<ParentsChildrenExtractShort> parentsChildrenExtractShortsModified = new ArrayList<>();
        List<ParentsChildrenExtractFull> parentsChildrenExtractFullsModified = new ArrayList<>();
        List<BirthExtractShort> birthExtractShortsModified = new ArrayList<>();
        List<BirthExtractFull> birthExtractFullsModified = new ArrayList<>();
        List<MarryExtractShort> marryExtractShortsModified = new ArrayList<>();
        List<MarryExtractFull> marryExtractFullsModified = new ArrayList<>();
        List<WedlockExtractShort> wedlockExtractShortsModified = new ArrayList<>();
        List<WedlockExtractFull> wedlockExtractFullsModified = new ArrayList<>();
        List<DeathExtractShort> deathExtractShortsModified = new ArrayList<>();
        List<DeathExtractFull> deathExtractFullsModified = new ArrayList<>();

        Map<Long, ParentsChildrenExtractFull> parentsChildrenExtractFullMap = parentsChildrenExtractFulls
                .stream()
                .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

        Map<Long, BirthExtractFull> birthExtractFullMap = birthExtractFulls
                .stream()
                .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

        Map<Long, MarryExtractFull> marryExtractFullMap = marryExtractFulls
                .stream()
                .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

        Map<Long, WedlockExtractFull> wedlockExtractFullMap = wedlockExtractFulls
                .stream()
                .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

        Map<Long, DeathExtractFull> deathExtractFullMap = deathExtractFulls
                .stream()
                .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

        // Khởi tạo AccessPoint để lưu vào các biểu mẫu
        AccessPoint accessPoint = new AccessPoint()
                .setProject(project)
                .setStatus(EAccessPointStatus.PROCESSING)
                .setCountExtractShort(countExtractShort)
                .setCountExtractFull(countExtractFull)
                .setTotalCount(totalCount);
        accessPointRepository.save(accessPoint);

        // Sắp xếp ngẫu nhiên danh sách users
        Collections.shuffle(users);

        int totalUsers = users.size();
        long[] countExtractPerUser = new long[totalUsers];

        // Phân phối số lượng biểu mẫu cho mỗi người dùng
        for (int i = 0; i < totalUsers; i++) {
            countExtractPerUser[i] = totalCount / totalUsers;
        }

        // Phân phối số lượng biểu mẫu còn lại
        for (int i = 0; i < totalCount % totalUsers; i++) {
            countExtractPerUser[i]++;
        }

        long[] countExtractShortPerUser = new long[totalUsers];
        long[] countExtractFullPerUser = new long[totalUsers];

        int userIndexShort = 0; // Index của user trường ngắn
        int userIndexFull = userIndexShort + 1; // Index của user trường dài

        long formsToAssignShort = countExtractPerUser[userIndexShort]; // Số lượng biểu mẫu cần phân phối cho user trường ngắn hiện tại
        long formsToAssignFull = countExtractPerUser[userIndexFull]; // Số lượng biểu mẫu cần phân phối cho user trường dài hiện tại
        
        long totalCountRemaining = totalCount / 2;

        // Phân phối ParentsChildrenExtractShort và ParentsChildrenExtractFull
        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            if (totalCountRemaining == 0) {
                break;
            }
            
            totalCountRemaining--;

            if (formsToAssignShort > 0 && formsToAssignFull > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                parentsChildrenExtractShortsModified.add(item);

                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullMap.get(item.getProjectNumberBookFile().getId());
                parentsChildrenExtractFull.setAccessPoint(accessPoint);
                parentsChildrenExtractFull.setImporter(users.get(userIndexFull));
                parentsChildrenExtractFullsModified.add(parentsChildrenExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort > 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexFull] == 0 && userIndexFull + 1 < totalUsers) {
                    userIndexFull++;
                }

                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                parentsChildrenExtractShortsModified.add(item);

                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullMap.get(item.getProjectNumberBookFile().getId());
                parentsChildrenExtractFull.setAccessPoint(accessPoint);
                parentsChildrenExtractFull.setImporter(users.get(userIndexFull));
                parentsChildrenExtractFullsModified.add(parentsChildrenExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }
            
            if (formsToAssignShort == 0 && formsToAssignFull > 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                parentsChildrenExtractShortsModified.add(item);

                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullMap.get(item.getProjectNumberBookFile().getId());
                parentsChildrenExtractFull.setAccessPoint(accessPoint);
                parentsChildrenExtractFull.setImporter(users.get(userIndexFull));
                parentsChildrenExtractFullsModified.add(parentsChildrenExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort == 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                while (countExtractPerUser[userIndexFull] == 0 || userIndexShort == userIndexFull) {
                    userIndexFull++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                parentsChildrenExtractShortsModified.add(item);

                ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullMap.get(item.getProjectNumberBookFile().getId());
                parentsChildrenExtractFull.setAccessPoint(accessPoint);
                parentsChildrenExtractFull.setImporter(users.get(userIndexFull));
                parentsChildrenExtractFullsModified.add(parentsChildrenExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;
            }
        }

        // Phân phối BirthExtractShort và BirthExtractFull
        for (BirthExtractShort item : birthExtractShorts) {
            if (totalCountRemaining == 0) {
                break;
            }

            totalCountRemaining--;

            if (formsToAssignShort > 0 && formsToAssignFull > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                birthExtractShortsModified.add(item);

                BirthExtractFull birthExtractFull = birthExtractFullMap.get(item.getProjectNumberBookFile().getId());
                birthExtractFull.setAccessPoint(accessPoint);
                birthExtractFull.setImporter(users.get(userIndexFull));
                birthExtractFullsModified.add(birthExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort > 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexFull] == 0 && userIndexFull + 1 < totalUsers) {
                    userIndexFull++;
                }

                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                birthExtractShortsModified.add(item);

                BirthExtractFull birthExtractFull = birthExtractFullMap.get(item.getProjectNumberBookFile().getId());
                birthExtractFull.setAccessPoint(accessPoint);
                birthExtractFull.setImporter(users.get(userIndexFull));
                birthExtractFullsModified.add(birthExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }
            
            if (formsToAssignShort == 0 && formsToAssignFull > 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                birthExtractShortsModified.add(item);

                BirthExtractFull birthExtractFull = birthExtractFullMap.get(item.getProjectNumberBookFile().getId());
                birthExtractFull.setAccessPoint(accessPoint);
                birthExtractFull.setImporter(users.get(userIndexFull));
                birthExtractFullsModified.add(birthExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort == 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                while (countExtractPerUser[userIndexFull] == 0 || userIndexShort == userIndexFull) {
                    userIndexFull++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                birthExtractShortsModified.add(item);

                BirthExtractFull birthExtractFull = birthExtractFullMap.get(item.getProjectNumberBookFile().getId());
                birthExtractFull.setAccessPoint(accessPoint);
                birthExtractFull.setImporter(users.get(userIndexFull));
                birthExtractFullsModified.add(birthExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;
            }
        }

        // Phân phối MarryExtractShort và MarryExtractFull
        for (MarryExtractShort item : marryExtractShorts) {
            if (totalCountRemaining == 0) {
                break;
            }

            totalCountRemaining--;

            if (formsToAssignShort > 0 && formsToAssignFull > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                marryExtractShortsModified.add(item);

                MarryExtractFull marryExtractFull = marryExtractFullMap.get(item.getProjectNumberBookFile().getId());
                marryExtractFull.setAccessPoint(accessPoint);
                marryExtractFull.setImporter(users.get(userIndexFull));
                marryExtractFullsModified.add(marryExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort > 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexFull] == 0 && userIndexFull + 1 < totalUsers) {
                    userIndexFull++;
                }

                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                marryExtractShortsModified.add(item);

                MarryExtractFull marryExtractFull = marryExtractFullMap.get(item.getProjectNumberBookFile().getId());
                marryExtractFull.setAccessPoint(accessPoint);
                marryExtractFull.setImporter(users.get(userIndexFull));
                marryExtractFullsModified.add(marryExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }
            
            if (formsToAssignShort == 0 && formsToAssignFull > 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                marryExtractShortsModified.add(item);

                MarryExtractFull marryExtractFull = marryExtractFullMap.get(item.getProjectNumberBookFile().getId());
                marryExtractFull.setAccessPoint(accessPoint);
                marryExtractFull.setImporter(users.get(userIndexFull));
                marryExtractFullsModified.add(marryExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }
            
            if (formsToAssignShort == 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                while (countExtractPerUser[userIndexFull] == 0 || userIndexShort == userIndexFull) {
                    userIndexFull++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                marryExtractShortsModified.add(item);

                MarryExtractFull marryExtractFull = marryExtractFullMap.get(item.getProjectNumberBookFile().getId());
                marryExtractFull.setAccessPoint(accessPoint);
                marryExtractFull.setImporter(users.get(userIndexFull));
                marryExtractFullsModified.add(marryExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;
            }
        }

        // Phân phối WedlockExtractShort và WedlockExtractFull
        for (WedlockExtractShort item : wedlockExtractShorts) {
            if (totalCountRemaining == 0) {
                break;
            }

            totalCountRemaining--;

            if (formsToAssignShort > 0 && formsToAssignFull > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                wedlockExtractShortsModified.add(item);

                WedlockExtractFull wedlockExtractFull = wedlockExtractFullMap.get(item.getProjectNumberBookFile().getId());
                wedlockExtractFull.setAccessPoint(accessPoint);
                wedlockExtractFull.setImporter(users.get(userIndexFull));
                wedlockExtractFullsModified.add(wedlockExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort > 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexFull] == 0 && userIndexFull + 1 < totalUsers) {
                    userIndexFull++;
                }

                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                wedlockExtractShortsModified.add(item);

                WedlockExtractFull wedlockExtractFull = wedlockExtractFullMap.get(item.getProjectNumberBookFile().getId());
                wedlockExtractFull.setAccessPoint(accessPoint);
                wedlockExtractFull.setImporter(users.get(userIndexFull));
                wedlockExtractFullsModified.add(wedlockExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort == 0 && formsToAssignFull > 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                wedlockExtractShortsModified.add(item);

                WedlockExtractFull wedlockExtractFull = wedlockExtractFullMap.get(item.getProjectNumberBookFile().getId());
                wedlockExtractFull.setAccessPoint(accessPoint);
                wedlockExtractFull.setImporter(users.get(userIndexFull));
                wedlockExtractFullsModified.add(wedlockExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort == 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                while (countExtractPerUser[userIndexFull] == 0 || userIndexShort == userIndexFull) {
                    userIndexFull++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                wedlockExtractShortsModified.add(item);

                WedlockExtractFull wedlockExtractFull = wedlockExtractFullMap.get(item.getProjectNumberBookFile().getId());
                wedlockExtractFull.setAccessPoint(accessPoint);
                wedlockExtractFull.setImporter(users.get(userIndexFull));
                wedlockExtractFullsModified.add(wedlockExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;
            }
        }

        // Phân phối DeathExtractShort và DeathExtractFull
        for (DeathExtractShort item : deathExtractShorts) {
            if (totalCountRemaining == 0) {
                break;
            }

            totalCountRemaining--;

            if (formsToAssignShort > 0 && formsToAssignFull > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                deathExtractShortsModified.add(item);

                DeathExtractFull deathExtractFull = deathExtractFullMap.get(item.getProjectNumberBookFile().getId());
                deathExtractFull.setAccessPoint(accessPoint);
                deathExtractFull.setImporter(users.get(userIndexFull));
                deathExtractFullsModified.add(deathExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort > 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexFull] == 0 && userIndexFull + 1 < totalUsers) {
                    userIndexFull++;
                }

                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                deathExtractShortsModified.add(item);

                DeathExtractFull deathExtractFull = deathExtractFullMap.get(item.getProjectNumberBookFile().getId());
                deathExtractFull.setAccessPoint(accessPoint);
                deathExtractFull.setImporter(users.get(userIndexFull));
                deathExtractFullsModified.add(deathExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort == 0 && formsToAssignFull > 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                deathExtractShortsModified.add(item);

                DeathExtractFull deathExtractFull = deathExtractFullMap.get(item.getProjectNumberBookFile().getId());
                deathExtractFull.setAccessPoint(accessPoint);
                deathExtractFull.setImporter(users.get(userIndexFull));
                deathExtractFullsModified.add(deathExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;

                continue;
            }

            if (formsToAssignShort == 0 && formsToAssignFull == 0) {
                while (countExtractPerUser[userIndexShort] == 0 && userIndexShort + 1 < totalUsers) {
                    userIndexShort++;
                }

                while (countExtractPerUser[userIndexFull] == 0 || userIndexShort == userIndexFull) {
                    userIndexFull++;
                }

                formsToAssignShort = countExtractPerUser[userIndexShort];
                formsToAssignFull = countExtractPerUser[userIndexFull];

                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndexShort));
                deathExtractShortsModified.add(item);

                DeathExtractFull deathExtractFull = deathExtractFullMap.get(item.getProjectNumberBookFile().getId());
                deathExtractFull.setAccessPoint(accessPoint);
                deathExtractFull.setImporter(users.get(userIndexFull));
                deathExtractFullsModified.add(deathExtractFull);

                formsToAssignShort--;
                countExtractPerUser[userIndexShort]--;

                formsToAssignFull--;
                countExtractPerUser[userIndexFull]--;

                countExtractShortPerUser[userIndexShort]++;
                countExtractShort++;

                countExtractFullPerUser[userIndexFull]++;
                countExtractFull++;
            }
        }

        // Lưu tất cả các đối tượng cùng một lúc sau khi xử lý xong
        if (!parentsChildrenExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractShortsModified);
        }

        if (!parentsChildrenExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractFullsModified);
        }

        if (!birthExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractShortsModified);
        }

        if (!birthExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractFullsModified);
        }

        if (!marryExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractShortsModified);
        }

        if (!marryExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractFullsModified);
        }

        if (!wedlockExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractShortsModified);
        }

        if (!wedlockExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractFullsModified);
        }

        if (!deathExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractShortsModified);
        }

        if (!deathExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractFullsModified);
        }

        // Cập nhật số lượng các biểu mẫu được phân phối cho AccessPoint
        accessPoint.setCountExtractShort(countExtractShort);
        accessPoint.setCountExtractFull(countExtractFull);
        accessPointRepository.save(accessPoint);

        // Lưu danh sách số lượng biểu mẫu phân phối cho từng user
        List<AccessPointHistory> accessPointHistories = new ArrayList<>();

        for (int i = 0; i < totalUsers; i++) {
            AccessPointHistory accessPointHistory = new AccessPointHistory()
                    .setAccessPoint(accessPoint)
                    .setProject(project)
                    .setCountExtractShort(countExtractShortPerUser[i])
                    .setCountExtractFull(countExtractFullPerUser[i])
                    .setAssignees(users.get(i))
                    .setTotalCount(countExtractShortPerUser[i] + countExtractFullPerUser[i]);
            accessPointHistories.add(accessPointHistory);
        }

        batchService.batchCreate(accessPointHistories);
    }

    @Override
    @Transactional
    public void assignExtractFormEachUserAndType(
            Project project,
            User user,
            List<ExtractFormCountTypeDTO> extractFormCountTypeDTOS
    ) {
        long totalCountExtractShort = 0L;
        long totalCountExtractFull = 0L;

        // Khởi tạo AccessPoint để lưu vào các biểu mẫu
        AccessPoint accessPoint = new AccessPoint()
                .setProject(project)
                .setStatus(EAccessPointStatus.PROCESSING)
                .setCountExtractShort(0L)
                .setCountExtractFull(0L)
                .setTotalCount(0L);
        accessPointRepository.save(accessPoint);

        //  Khởi tạo danh sách các đối tượng đã cập nhật để lưu vào db
        List<ParentsChildrenExtractShort> parentsChildrenExtractShortsModified = new ArrayList<>();
        List<ParentsChildrenExtractFull> parentsChildrenExtractFullsModified = new ArrayList<>();
        List<BirthExtractShort> birthExtractShortsModified = new ArrayList<>();
        List<BirthExtractFull> birthExtractFullsModified = new ArrayList<>();
        List<MarryExtractShort> marryExtractShortsModified = new ArrayList<>();
        List<MarryExtractFull> marryExtractFullsModified = new ArrayList<>();
        List<WedlockExtractShort> wedlockExtractShortsModified = new ArrayList<>();
        List<WedlockExtractFull> wedlockExtractFullsModified = new ArrayList<>();
        List<DeathExtractShort> deathExtractShortsModified = new ArrayList<>();
        List<DeathExtractFull> deathExtractFullsModified = new ArrayList<>();

        for (ExtractFormCountTypeDTO type : extractFormCountTypeDTOS) {
            ERegistrationType registrationType = ERegistrationType.valueOf(type.getRegistrationType());

            switch (registrationType) {
                case CMC -> {
                    long totalCountParentsChildren = Long.parseLong(type.getTotalCount());
                    long totalCountParentsChildrenShort = totalCountParentsChildren /2;
                    long totalCountParentsChildrenFull = totalCountParentsChildren / 2;

                    List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByProjectAndAccessPointIsNull(project);

                    List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndAccessPointIsNull(project);

                    long countParentsChildrenRemaining = parentsChildrenExtractShorts.size() + parentsChildrenExtractFulls.size();

                    if (totalCountParentsChildren > countParentsChildrenRemaining) {
                        throw new DataInputException("Tổng số phiếu nhận cha mẹ con cần nhập chỉ còn " + countParentsChildrenRemaining + " phiếu");
                    }

                    totalCountExtractShort += totalCountParentsChildrenShort;
                    totalCountExtractFull += totalCountParentsChildrenFull;

                    Map<Long, ParentsChildrenExtractFull> parentsChildrenExtractFullMap = parentsChildrenExtractFulls
                            .stream()
                            .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

                    // Phân phối ParentsChildrenExtractShort và ParentsChildrenExtractFull
                    for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
                        if (totalCountParentsChildrenShort == 0) {
                            break;
                        }

                        totalCountParentsChildrenShort--;

                        item.setAccessPoint(accessPoint);
                        item.setImporter(user);
                        parentsChildrenExtractShortsModified.add(item);
                    }

                    for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
                        if (totalCountParentsChildrenFull == 0) {
                            break;
                        }

                        totalCountParentsChildrenFull--;

                        ParentsChildrenExtractFull parentsChildrenExtractFull = parentsChildrenExtractFullMap.get(
                                item.getProjectNumberBookFile().getId()
                        );

                        parentsChildrenExtractFull.setAccessPoint(accessPoint);
                        parentsChildrenExtractFull.setImporter(user);
                        parentsChildrenExtractFullsModified.add(parentsChildrenExtractFull);
                    }
                }
                case KS -> {
                    long totalCountBirth = Long.parseLong(type.getTotalCount());
                    long totalCountBirthShort = totalCountBirth /2;
                    long totalCountBirthFull = totalCountBirth / 2;

                    List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByProjectAndAccessPointIsNull(project);

                    List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndAccessPointIsNull(project);

                    long countBirthRemaining = birthExtractShorts.size() + birthExtractFulls.size();

                    if (totalCountBirth > countBirthRemaining) {
                        throw new DataInputException("Tổng số phiếu khai sinh cần nhập chỉ còn " + countBirthRemaining + " phiếu");
                    }

                    totalCountExtractShort += totalCountBirthShort;
                    totalCountExtractFull += totalCountBirthFull;

                    Map<Long, BirthExtractFull> birthExtractFullMap = birthExtractFulls
                            .stream()
                            .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

                    // Phân phối BirthExtractShort và BirthExtractFull
                    for (BirthExtractShort item : birthExtractShorts) {
                        if (totalCountBirthShort == 0) {
                            break;
                        }

                        totalCountBirthShort--;

                        item.setAccessPoint(accessPoint);
                        item.setImporter(user);
                        birthExtractShortsModified.add(item);
                    }

                    for (BirthExtractFull item : birthExtractFulls) {
                        if (totalCountBirthFull == 0) {
                            break;
                        }

                        totalCountBirthFull--;

                        BirthExtractFull birthExtractFull = birthExtractFullMap.get(
                                item.getProjectNumberBookFile().getId()
                        );

                        birthExtractFull.setAccessPoint(accessPoint);
                        birthExtractFull.setImporter(user);
                        birthExtractFullsModified.add(birthExtractFull);
                    }
                }
                case KH -> {
                    long totalCountMarry = Long.parseLong(type.getTotalCount());
                    long totalCountMarryShort = totalCountMarry /2;
                    long totalCountMarryFull = totalCountMarry / 2;

                    List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findAllByProjectAndAccessPointIsNull(project);

                    List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndAccessPointIsNull(project);

                    long countMarryRemaining = marryExtractShorts.size() + marryExtractFulls.size();

                    if (totalCountMarry > countMarryRemaining) {
                        throw new DataInputException("Tổng số phiếu kết hôn cần nhập chỉ còn " + countMarryRemaining + " phiếu");
                    }

                    totalCountExtractShort += totalCountMarryShort;
                    totalCountExtractFull += totalCountMarryFull;

                    Map<Long, MarryExtractFull> marryExtractFullMap = marryExtractFulls
                            .stream()
                            .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

                    // Phân phối MarryExtractShort và MarryExtractFull
                    for (MarryExtractShort item : marryExtractShorts) {
                        if (totalCountMarryShort == 0) {
                            break;
                        }

                        totalCountMarryShort--;

                        item.setAccessPoint(accessPoint);
                        item.setImporter(user);
                        marryExtractShortsModified.add(item);
                    }

                    for (MarryExtractFull item : marryExtractFulls) {
                        if (totalCountMarryFull == 0) {
                            break;
                        }

                        totalCountMarryFull--;

                        MarryExtractFull marryExtractFull = marryExtractFullMap.get(
                                item.getProjectNumberBookFile().getId()
                        );

                        marryExtractFull.setAccessPoint(accessPoint);
                        marryExtractFull.setImporter(user);
                        marryExtractFullsModified.add(marryExtractFull);
                    }
                }
                case HN -> {
                    long totalCountWedlock = Long.parseLong(type.getTotalCount());
                    long totalCountWedlockShort = totalCountWedlock /2;
                    long totalCountWedlockFull = totalCountWedlock / 2;

                    List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByProjectAndAccessPointIsNull(project);

                    List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndAccessPointIsNull(project);

                    long countWedlockRemaining = wedlockExtractShorts.size() + wedlockExtractFulls.size();

                    if (totalCountWedlock > countWedlockRemaining) {
                        throw new DataInputException("Tổng số phiếu tình trạng hôn nhân cần nhập chỉ còn " + countWedlockRemaining + " phiếu");
                    }

                    totalCountExtractShort += totalCountWedlockShort;
                    totalCountExtractFull += totalCountWedlockFull;

                    Map<Long, WedlockExtractFull> wedlockExtractFullMap = wedlockExtractFulls
                            .stream()
                            .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

                    // Phân phối WedlockExtractShort và WedlockExtractFull
                    for (WedlockExtractShort item : wedlockExtractShorts) {
                        if (totalCountWedlockShort == 0) {
                            break;
                        }

                        totalCountWedlockShort--;

                        item.setAccessPoint(accessPoint);
                        item.setImporter(user);
                        wedlockExtractShortsModified.add(item);
                    }

                    for (WedlockExtractFull item : wedlockExtractFulls) {
                        if (totalCountWedlockFull == 0) {
                            break;
                        }

                        totalCountWedlockFull--;

                        WedlockExtractFull wedlockExtractFull = wedlockExtractFullMap.get(
                                item.getProjectNumberBookFile().getId()
                        );

                        wedlockExtractFull.setAccessPoint(accessPoint);
                        wedlockExtractFull.setImporter(user);
                        wedlockExtractFullsModified.add(wedlockExtractFull);
                    }
                }
                case KT -> {
                    long totalCountDeath = Long.parseLong(type.getTotalCount());
                    long totalCountDeathShort = totalCountDeath /2;
                    long totalCountDeathFull = totalCountDeath / 2;

                    List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByProjectAndAccessPointIsNull(project);

                    List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndAccessPointIsNull(project);

                    long countDeathRemaining = deathExtractShorts.size() + deathExtractFulls.size();

                    if (totalCountDeath > countDeathRemaining) {
                        throw new DataInputException("Tổng số phiếu khai tử cần nhập chỉ còn " + countDeathRemaining + " phiếu");
                    }

                    totalCountExtractShort += totalCountDeathShort;
                    totalCountExtractFull += totalCountDeathFull;

                    Map<Long, DeathExtractFull> deathExtractFullMap = deathExtractFulls
                            .stream()
                            .collect(Collectors.toMap(item -> item.getProjectNumberBookFile().getId(), Function.identity()));

                    // Phân phối DeathExtractShort và DeathExtractFull
                    for (DeathExtractShort item : deathExtractShorts) {
                        if (totalCountDeathShort == 0) {
                            break;
                        }

                        totalCountDeathShort--;

                        item.setAccessPoint(accessPoint);
                        item.setImporter(user);
                        deathExtractShortsModified.add(item);
                    }

                    for (DeathExtractFull item : deathExtractFulls) {
                        if (totalCountDeathFull == 0) {
                            break;
                        }

                        totalCountDeathFull--;

                        DeathExtractFull deathExtractFull = deathExtractFullMap.get(
                                item.getProjectNumberBookFile().getId()
                        );

                        deathExtractFull.setAccessPoint(accessPoint);
                        deathExtractFull.setImporter(user);
                        deathExtractFullsModified.add(deathExtractFull);
                    }
                }
            }
        }

        // Lưu tất cả các đối tượng cùng một lúc sau khi xử lý xong
        if (!parentsChildrenExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractShortsModified);
        }

        if (!parentsChildrenExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractFullsModified);
        }

        if (!birthExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractShortsModified);
        }

        if (!birthExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractFullsModified);
        }

        if (!marryExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractShortsModified);
        }

        if (!marryExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractFullsModified);
        }

        if (!wedlockExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractShortsModified);
        }

        if (!wedlockExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractFullsModified);
        }

        if (!deathExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractShortsModified);
        }

        if (!deathExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractFullsModified);
        }

        // Cập nhật số lượng các biểu mẫu được phân phối cho AccessPoint
        accessPoint.setCountExtractShort(totalCountExtractShort);
        accessPoint.setCountExtractFull(totalCountExtractFull);
        accessPoint.setTotalCount(totalCountExtractShort + totalCountExtractFull);
        accessPointRepository.save(accessPoint);

        AccessPointHistory accessPointHistory = new AccessPointHistory()
                .setAccessPoint(accessPoint)
                .setProject(project)
                .setCountExtractShort(totalCountExtractShort)
                .setCountExtractFull(totalCountExtractFull)
                .setAssignees(user)
                .setTotalCount(totalCountExtractShort + totalCountExtractFull);
        accessPointHistoryRepository.save(accessPointHistory);
    }

    @Override
    @Transactional
    public void autoCompareExtractShortFull(AccessPoint accessPoint) {
        List<ParentsChildrenExtractShort> parentsChildrenExtractShortsModified = new ArrayList<>();
        List<ParentsChildrenExtractFull> parentsChildrenExtractFullsModified = new ArrayList<>();
        List<BirthExtractShort> birthExtractShortsModified = new ArrayList<>();
        List<BirthExtractFull> birthExtractFullsModified = new ArrayList<>();
        List<MarryExtractShort> marryExtractShortsModified = new ArrayList<>();
        List<MarryExtractFull> marryExtractFullsModified = new ArrayList<>();
        List<WedlockExtractShort> wedlockExtractShortsModified = new ArrayList<>();
        List<WedlockExtractFull> wedlockExtractFullsModified = new ArrayList<>();
        List<DeathExtractShort> deathExtractShortsModified = new ArrayList<>();
        List<DeathExtractFull> deathExtractFullsModified = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);

        Map<String, AccessPointHistory> accessPointHistoryMap = accessPointHistoryRepository.findByAccessPoint(accessPoint)
                .stream()
                .collect(Collectors.toMap(history -> history.getAssignees().getId(), Function.identity()));

        for (ParentsChildrenExtractShort parentsChildrenExtractShort : parentsChildrenExtractShorts) {
            ProjectNumberBookFile projectNumberBookFile = parentsChildrenExtractShort.getProjectNumberBookFile();
            Optional<ParentsChildrenExtractFull> parentsChildrenExtractFullOptional = parentsChildrenExtractFullRepository.findByProjectNumberBookFileAndStatusAndImporterIsNotNull(projectNumberBookFile, EInputStatus.IMPORTED);

            if (parentsChildrenExtractFullOptional.isPresent()) {
                ParentsChildrenCompareCommonDTO parentsChildrenCompareExtractFull = modelMapper.map(
                        parentsChildrenExtractFullOptional.get(),
                        ParentsChildrenCompareCommonDTO.class
                );

                ParentsChildrenCompareCommonDTO parentsChildrenCompareExtractShort = modelMapper.map(
                        parentsChildrenExtractShort,
                        ParentsChildrenCompareCommonDTO.class
                );

                if (parentsChildrenCompareExtractFull != null && parentsChildrenCompareExtractShort != null) {
                    boolean isMatching = appUtils.compareFields(parentsChildrenCompareExtractFull, parentsChildrenCompareExtractShort);

                    AccessPointHistory accessPointHistoryFull = accessPointHistoryMap.get(parentsChildrenExtractFullOptional.get().getImporter().getId());
                    AccessPointHistory accessPointHistoryShort = accessPointHistoryMap.get(parentsChildrenExtractShort.getImporter().getId());

                    if (isMatching) {
                        // Cập nhật trạng thái 2 phiếu trùng khớp dữ liệu
                        parentsChildrenExtractShort.setStatus(EInputStatus.MATCHING);
                        parentsChildrenExtractFullOptional.get().setStatus(EInputStatus.MATCHING);

                        // Cập nhật số lượng thành công
                        accessPointHistoryShort.setCountSuccessExtractShort(accessPointHistoryShort.getCountSuccessExtractShort() + 1);
                        accessPointHistoryFull.setCountSuccessExtractFull(accessPointHistoryFull.getCountSuccessExtractFull() + 1);

                        accessPoint.setTotalSuccessExtractFull(accessPoint.getTotalSuccessExtractFull() + 1);
                        accessPoint.setTotalSuccessExtractShort(accessPoint.getTotalSuccessExtractShort() + 1);
                    } else {
                        // Cập nhật trạng thái 2 phiếu không trùng khớp dữ liệu
                        parentsChildrenExtractShort.setStatus(EInputStatus.NOT_MATCHING);
                        parentsChildrenExtractFullOptional.get().setStatus(EInputStatus.NOT_MATCHING);

                        // Cập nhật số lượng lỗi
                        accessPointHistoryShort.setCountErrorExtractShort(accessPointHistoryShort.getCountErrorExtractShort() + 1);
                        accessPointHistoryFull.setCountErrorExtractFull(accessPointHistoryFull.getCountErrorExtractFull() + 1);

                        accessPoint.setTotalErrorExtractFull(accessPoint.getTotalErrorExtractFull() + 1);
                        accessPoint.setTotalErrorExtractShort(accessPoint.getTotalErrorExtractShort() + 1);
                    }

                    // Thêm đối tượng vào danh sách sau khi xác định trạng thái
                    parentsChildrenExtractShortsModified.add(parentsChildrenExtractShort);
                    parentsChildrenExtractFullsModified.add(parentsChildrenExtractFullOptional.get());
                }
            }
        }

        for (BirthExtractShort birthExtractShort : birthExtractShorts) {
            ProjectNumberBookFile projectNumberBookFile = birthExtractShort.getProjectNumberBookFile();
            Optional<BirthExtractFull> birthExtractFullOptional = birthExtractFullRepository.findByProjectNumberBookFileAndStatusAndImporterIsNotNull(projectNumberBookFile, EInputStatus.IMPORTED);

            if (birthExtractFullOptional.isPresent()) {
                BirthCompareCommonDTO birthCompareExtractFull = modelMapper.map(
                        birthExtractFullOptional.get(),
                        BirthCompareCommonDTO.class
                );

                BirthCompareCommonDTO birthCompareExtractShort = modelMapper.map(
                        birthExtractShort,
                        BirthCompareCommonDTO.class
                );

                if (birthCompareExtractFull != null && birthCompareExtractShort != null) {
                    boolean isMatching = appUtils.compareFields(birthCompareExtractFull, birthCompareExtractShort);

                    AccessPointHistory accessPointHistoryFull = accessPointHistoryMap.get(birthExtractFullOptional.get().getImporter().getId());
                    AccessPointHistory accessPointHistoryShort = accessPointHistoryMap.get(birthExtractShort.getImporter().getId());

                    if (isMatching) {
                        // Cập nhật trạng thái 2 phiếu trùng khớp dữ liệu
                        birthExtractShort.setStatus(EInputStatus.MATCHING);
                        birthExtractFullOptional.get().setStatus(EInputStatus.MATCHING);

                        // Cập nhật số lượng thành công
                        accessPointHistoryShort.setCountSuccessExtractShort(accessPointHistoryShort.getCountSuccessExtractShort() + 1);
                        accessPointHistoryFull.setCountSuccessExtractFull(accessPointHistoryFull.getCountSuccessExtractFull() + 1);

                        accessPoint.setTotalSuccessExtractFull(accessPoint.getTotalSuccessExtractFull() + 1);
                        accessPoint.setTotalSuccessExtractShort(accessPoint.getTotalSuccessExtractShort() + 1);
                    } else {
                        // Cập nhật trạng thái 2 phiếu không trùng khớp dữ liệu
                        birthExtractShort.setStatus(EInputStatus.NOT_MATCHING);
                        birthExtractFullOptional.get().setStatus(EInputStatus.NOT_MATCHING);

                        // Cập nhật số lượng lỗi
                        accessPointHistoryShort.setCountErrorExtractShort(accessPointHistoryShort.getCountErrorExtractShort() + 1);
                        accessPointHistoryFull.setCountErrorExtractFull(accessPointHistoryFull.getCountErrorExtractFull() + 1);

                        accessPoint.setTotalErrorExtractFull(accessPoint.getTotalErrorExtractFull() + 1);
                        accessPoint.setTotalErrorExtractShort(accessPoint.getTotalErrorExtractShort() + 1);
                    }

                    // Thêm đối tượng vào danh sách sau khi xác định trạng thái
                    birthExtractShortsModified.add(birthExtractShort);
                    birthExtractFullsModified.add(birthExtractFullOptional.get());
                }
            }
        }

        for (MarryExtractShort marryExtractShort : marryExtractShorts) {
            ProjectNumberBookFile projectNumberBookFile = marryExtractShort.getProjectNumberBookFile();
            Optional<MarryExtractFull> marryExtractFullOptional = marryExtractFullRepository.findByProjectNumberBookFileAndStatusAndImporterIsNotNull(projectNumberBookFile, EInputStatus.IMPORTED);

            if (marryExtractFullOptional.isPresent()) {
                MarryCompareCommonDTO marryCompareExtractFull = modelMapper.map(
                        marryExtractFullOptional.get(),
                        MarryCompareCommonDTO.class
                );

                MarryCompareCommonDTO marryCompareExtractShort = modelMapper.map(
                        marryExtractShort,
                        MarryCompareCommonDTO.class
                );

                if (marryCompareExtractFull != null && marryCompareExtractShort != null) {
                    boolean isMatching = appUtils.compareFields(marryCompareExtractFull, marryCompareExtractShort);

                    AccessPointHistory accessPointHistoryFull = accessPointHistoryMap.get(marryExtractFullOptional.get().getImporter().getId());
                    AccessPointHistory accessPointHistoryShort = accessPointHistoryMap.get(marryExtractShort.getImporter().getId());

                    if (isMatching) {
                        // Cập nhật trạng thái 2 phiếu trùng khớp dữ liệu
                        marryExtractShort.setStatus(EInputStatus.MATCHING);
                        marryExtractFullOptional.get().setStatus(EInputStatus.MATCHING);

                        // Cập nhật số lượng thành công
                        accessPointHistoryShort.setCountSuccessExtractShort(accessPointHistoryShort.getCountSuccessExtractShort() + 1);
                        accessPointHistoryFull.setCountSuccessExtractFull(accessPointHistoryFull.getCountSuccessExtractFull() + 1);

                        accessPoint.setTotalSuccessExtractFull(accessPoint.getTotalSuccessExtractFull() + 1);
                        accessPoint.setTotalSuccessExtractShort(accessPoint.getTotalSuccessExtractShort() + 1);
                    } else {
                        // Cập nhật trạng thái 2 phiếu không trùng khớp dữ liệu
                        marryExtractShort.setStatus(EInputStatus.NOT_MATCHING);
                        marryExtractFullOptional.get().setStatus(EInputStatus.NOT_MATCHING);

                        // Cập nhật số lượng lỗi
                        accessPointHistoryShort.setCountErrorExtractShort(accessPointHistoryShort.getCountErrorExtractShort() + 1);
                        accessPointHistoryFull.setCountErrorExtractFull(accessPointHistoryFull.getCountErrorExtractFull() + 1);

                        accessPoint.setTotalErrorExtractFull(accessPoint.getTotalErrorExtractFull() + 1);
                        accessPoint.setTotalErrorExtractShort(accessPoint.getTotalErrorExtractShort() + 1);
                    }

                    // Thêm đối tượng vào danh sách sau khi xác định trạng thái
                    marryExtractShortsModified.add(marryExtractShort);
                    marryExtractFullsModified.add(marryExtractFullOptional.get());
                }
            }
        }

        for (WedlockExtractShort wedlockExtractShort : wedlockExtractShorts) {
            ProjectNumberBookFile projectNumberBookFile = wedlockExtractShort.getProjectNumberBookFile();
            Optional<WedlockExtractFull> wedlockExtractFullOptional = wedlockExtractFullRepository.findByProjectNumberBookFileAndStatusAndImporterIsNotNull(projectNumberBookFile, EInputStatus.IMPORTED);

            if (wedlockExtractFullOptional.isPresent()) {
                WedlockCompareCommonDTO wedlockCompareExtractFull = modelMapper.map(
                        wedlockExtractFullOptional.get(),
                        WedlockCompareCommonDTO.class
                );

                WedlockCompareCommonDTO wedlockCompareExtractShort = modelMapper.map(
                        wedlockExtractShort,
                        WedlockCompareCommonDTO.class
                );

                if (wedlockCompareExtractFull != null && wedlockCompareExtractShort != null) {
                    boolean isMatching = appUtils.compareFields(wedlockCompareExtractFull, wedlockCompareExtractShort);

                    AccessPointHistory accessPointHistoryFull = accessPointHistoryMap.get(wedlockExtractFullOptional.get().getImporter().getId());
                    AccessPointHistory accessPointHistoryShort = accessPointHistoryMap.get(wedlockExtractShort.getImporter().getId());

                    if (isMatching) {
                        // Cập nhật trạng thái 2 phiếu trùng khớp dữ liệu
                        wedlockExtractShort.setStatus(EInputStatus.MATCHING);
                        wedlockExtractFullOptional.get().setStatus(EInputStatus.MATCHING);

                        // Cập nhật số lượng thành công
                        accessPointHistoryShort.setCountSuccessExtractShort(accessPointHistoryShort.getCountSuccessExtractShort() + 1);
                        accessPointHistoryFull.setCountSuccessExtractFull(accessPointHistoryFull.getCountSuccessExtractFull() + 1);

                        accessPoint.setTotalSuccessExtractFull(accessPoint.getTotalSuccessExtractFull() + 1);
                        accessPoint.setTotalSuccessExtractShort(accessPoint.getTotalSuccessExtractShort() + 1);
                    } else {
                        // Cập nhật trạng thái 2 phiếu không trùng khớp dữ liệu
                        wedlockExtractShort.setStatus(EInputStatus.NOT_MATCHING);
                        wedlockExtractFullOptional.get().setStatus(EInputStatus.NOT_MATCHING);

                        // Cập nhật số lượng lỗi
                        accessPointHistoryShort.setCountErrorExtractShort(accessPointHistoryShort.getCountErrorExtractShort() + 1);
                        accessPointHistoryFull.setCountErrorExtractFull(accessPointHistoryFull.getCountErrorExtractFull() + 1);

                        accessPoint.setTotalErrorExtractFull(accessPoint.getTotalErrorExtractFull() + 1);
                        accessPoint.setTotalErrorExtractShort(accessPoint.getTotalErrorExtractShort() + 1);
                    }

                    // Thêm đối tượng vào danh sách sau khi xác định trạng thái
                    wedlockExtractShortsModified.add(wedlockExtractShort);
                    wedlockExtractFullsModified.add(wedlockExtractFullOptional.get());
                }
            }
        }

        for (DeathExtractShort deathExtractShort : deathExtractShorts) {
            ProjectNumberBookFile projectNumberBookFile = deathExtractShort.getProjectNumberBookFile();
            Optional<DeathExtractFull> deathExtractFullOptional = deathExtractFullRepository.findByProjectNumberBookFileAndStatusAndImporterIsNotNull(projectNumberBookFile, EInputStatus.IMPORTED);

            if (deathExtractFullOptional.isPresent()) {
                DeathCompareCommonDTO deathCompareExtractFull = modelMapper.map(
                        deathExtractFullOptional.get(),
                        DeathCompareCommonDTO.class
                );

                DeathCompareCommonDTO deathCompareExtractShort = modelMapper.map(
                        deathExtractShort,
                        DeathCompareCommonDTO.class
                );

                if (deathCompareExtractFull != null && deathCompareExtractShort != null) {
                    boolean isMatching = appUtils.compareFields(deathCompareExtractFull, deathCompareExtractShort);

                    AccessPointHistory accessPointHistoryFull = accessPointHistoryMap.get(deathExtractFullOptional.get().getImporter().getId());
                    AccessPointHistory accessPointHistoryShort = accessPointHistoryMap.get(deathExtractShort.getImporter().getId());

                    if (isMatching) {
                        // Cập nhật trạng thái 2 phiếu trùng khớp dữ liệu
                        deathExtractShort.setStatus(EInputStatus.MATCHING);
                        deathExtractFullOptional.get().setStatus(EInputStatus.MATCHING);

                        // Cập nhật số lượng thành công
                        accessPointHistoryShort.setCountSuccessExtractShort(accessPointHistoryShort.getCountSuccessExtractShort() + 1);
                        accessPointHistoryFull.setCountSuccessExtractFull(accessPointHistoryFull.getCountSuccessExtractFull() + 1);

                        accessPoint.setTotalSuccessExtractFull(accessPoint.getTotalSuccessExtractFull() + 1);
                        accessPoint.setTotalSuccessExtractShort(accessPoint.getTotalSuccessExtractShort() + 1);
                    } else {
                        // Cập nhật trạng thái 2 phiếu không trùng khớp dữ liệu
                        deathExtractShort.setStatus(EInputStatus.NOT_MATCHING);
                        deathExtractFullOptional.get().setStatus(EInputStatus.NOT_MATCHING);

                        // Cập nhật số lượng lỗi
                        accessPointHistoryShort.setCountErrorExtractShort(accessPointHistoryShort.getCountErrorExtractShort() + 1);
                        accessPointHistoryFull.setCountErrorExtractFull(accessPointHistoryFull.getCountErrorExtractFull() + 1);

                        accessPoint.setTotalErrorExtractFull(accessPoint.getTotalErrorExtractFull() + 1);
                        accessPoint.setTotalErrorExtractShort(accessPoint.getTotalErrorExtractShort() + 1);
                    }

                    // Thêm đối tượng vào danh sách sau khi xác định trạng thái
                    deathExtractShortsModified.add(deathExtractShort);
                    deathExtractFullsModified.add(deathExtractFullOptional.get());
                }
            }
        }

        // Lưu tất cả các đối tượng cùng một lúc sau khi xử lý xong
        if (!parentsChildrenExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractShortsModified);
        }

        if (!parentsChildrenExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractFullsModified);
        }

        if (!birthExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractShortsModified);
        }

        if (!birthExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractFullsModified);
        }

        if (!marryExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractShortsModified);
        }

        if (!marryExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractFullsModified);
        }

        if (!wedlockExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractShortsModified);
        }

        if (!wedlockExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractFullsModified);
        }

        if (!deathExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractShortsModified);
        }

        if (!deathExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractFullsModified);
        }

        accessPointHistoryRepository.saveAll(accessPointHistoryMap.values());

        long totalSuccessError = accessPoint.getTotalSuccessExtractFull() +
                accessPoint.getTotalSuccessExtractShort() +
                accessPoint.getTotalErrorExtractFull() +
                accessPoint.getTotalErrorExtractShort();

        if (accessPoint.getTotalCount() == totalSuccessError) {
            accessPoint.setStatus(EAccessPointStatus.FULL);
        }

        accessPointRepository.save(accessPoint);
    }

    @Override
    public void delete(Project project) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
