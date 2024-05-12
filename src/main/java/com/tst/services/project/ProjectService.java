package com.tst.services.project;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.compare.*;
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
    public List<ExtractFullResponse> findAllExtractFullResponse(Project project, User importer) {
        List<ExtractFullResponse> extractFullResponses = new ArrayList<>();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporter(project, importer);

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllByProjectAndImporter(project, importer);

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
//            String inputStatus = item.getStatus() == EInputStatus.NEW
//                    ? "Chưa nhập"
//                    : item.getStatus() == EInputStatus.LATER_PROCESSING
//                    ? "Xử lý sau"
//                    : "Đã nhập";
//
//            String checkedStatus = item.getStatus() == EInputStatus.MATCHING
//                    ? "Đúng"
//                    : item.getStatus() == EInputStatus.NOT_MATCHING
//                    ? "Sai"
//                    : "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
//            String inputStatus = item.getStatus() == EInputStatus.NEW
//                    ? "Chưa nhập"
//                    : item.getStatus() == EInputStatus.LATER_PROCESSING
//                    ? "Xử lý sau"
//                    : "Đã nhập";
//
//            String checkedStatus = item.getStatus() == EInputStatus.MATCHING
//                    ? "Đúng"
//                    : item.getStatus() == EInputStatus.NOT_MATCHING
//                    ? "Sai"
//                    : "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
//            String inputStatus = item.getStatus() == EInputStatus.NEW
//                    ? "Chưa nhập"
//                    : item.getStatus() == EInputStatus.LATER_PROCESSING
//                    ? "Xử lý sau"
//                    : "Đã nhập";
//
//            String checkedStatus = item.getStatus() == EInputStatus.MATCHING
//                    ? "Đúng"
//                    : item.getStatus() == EInputStatus.NOT_MATCHING
//                    ? "Sai"
//                    : "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
//            String inputStatus = item.getStatus() == EInputStatus.NEW
//                    ? "Chưa nhập"
//                    : item.getStatus() == EInputStatus.LATER_PROCESSING
//                    ? "Xử lý sau"
//                    : "Đã nhập";
//
//            String checkedStatus = item.getStatus() == EInputStatus.MATCHING
//                    ? "Đúng"
//                    : item.getStatus() == EInputStatus.NOT_MATCHING
//                    ? "Sai"
//                    : "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
//            String inputStatus = item.getStatus() == EInputStatus.NEW
//                    ? "Chưa nhập"
//                    : item.getStatus() == EInputStatus.LATER_PROCESSING
//                    ? "Xử lý sau"
//                    : "Đã nhập";
//
//            String checkedStatus = item.getStatus() == EInputStatus.MATCHING
//                    ? "Đúng"
//                    : item.getStatus() == EInputStatus.NOT_MATCHING
//                    ? "Sai"
//                    : "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(item.getStatus().getValue())
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(item.getStatus().getValue())
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<ExtractShortResponse> findAllNewExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findByProjectAndImporterAndStatusNew(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findByProjectAndImporterAndStatusNew(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterAndStatusNew(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findByProjectAndImporterAndStatusNew(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findByProjectAndImporterAndStatusNew(project, importer);

        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

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
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            String inputStatus = "Chưa nhập";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<ExtractShortResponse> findAllLaterExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findByProjectAndImporterAndStatusLater(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findByProjectAndImporterAndStatusLater(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterAndStatusLater(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findByProjectAndImporterAndStatusLater(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findByProjectAndImporterAndStatusLater(project, importer);


        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractShortResponse extractShortResponse = new ExtractShortResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

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
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            String inputStatus = "Xử lý sau";
            String checkedStatus = "";

            ExtractFullResponse extractFullResponse = new ExtractFullResponse()
                    .setId(item.getId())
                    .setFolderPath(item.getProjectNumberBookFile().getFolderPath())
                    .setFileName(item.getProjectNumberBookFile().getFileName())
                    .setRegistrationType(item.getProjectNumberBookFile().getRegistrationType().getValue())
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    public List<ExtractShortResponse> findAllImportedExtractShortResponse(Project project, User importer) {
        List<ExtractShortResponse> extractShortResponses = new ArrayList<>();

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findByProjectAndImporterAndStatusImported(project, importer);

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findByProjectAndImporterAndStatusImported(project, importer);

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterAndStatusImported(project, importer);

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findByProjectAndImporterAndStatusImported(project, importer);

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findByProjectAndImporterAndStatusImported(project, importer);


        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (BirthExtractShort item : birthExtractShorts) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (MarryExtractShort item : marryExtractShorts) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractShortResponses.add(extractShortResponse);
        }

        for (DeathExtractShort item : deathExtractShorts) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

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
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (BirthExtractFull item : birthExtractFulls) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (MarryExtractFull item : marryExtractFulls) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        for (DeathExtractFull item : deathExtractFulls) {
            String inputStatus = "Đã nhập";
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
                    .setInputStatus(inputStatus)
                    .setImportedAt(item.getImportedAt())
                    .setCheckedStatus(checkedStatus)
                    .setCheckedAt(item.getCheckedAt());

            extractFullResponses.add(extractFullResponse);
        }

        return extractFullResponses;
    }

    @Override
    @Transactional
    public void createRegistrationPoint(Project project, LocationProvince locationProvince, LocationDistrict locationDistrict, LocationWard locationWard) {
        ProjectProvince projectProvince = new ProjectProvince()
                .setProject(project)
                .setCode(locationProvince.getCode())
                .setName(locationProvince.getName());
        projectProvinceRepository.save(projectProvince);

        ProjectDistrict projectDistrict = new ProjectDistrict()
                .setProjectProvince(projectProvince)
                .setName(locationDistrict.getName())
                .setCode(locationDistrict.getCode());
        projectDistrictRepository.save(projectDistrict);

        ProjectWard projectWard = new ProjectWard()
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
                registrationType.getCode()
        );

        if (projectRegistrationTypeOptional.isEmpty()) {
            ProjectRegistrationType projectRegistrationType = new ProjectRegistrationType()
                    .setProjectWard(projectWard)
                    .setCode(registrationType.getCode());
            projectRegistrationTypeRepository.save(projectRegistrationType);

            ProjectPaperSize projectPaperSize = new ProjectPaperSize()
                    .setProjectRegistrationType(projectRegistrationType)
                    .setCode(ePaperSize);
            projectPaperSizeRepository.save(projectPaperSize);

            ProjectRegistrationDate projectRegistrationDate = new ProjectRegistrationDate()
                    .setProjectPaperSize(projectPaperSize)
                    .setCode(registrationDateCode);
            projectRegistrationDateRepository.save(projectRegistrationDate);

            this.uploadCoverFileAndSave(
                    projectWard,
                    projectRegistrationType,
                    projectPaperSize,
                    projectRegistrationDate,
                    numberBookCode,
                    coverFile
            );
        }
        else {
            Optional<ProjectPaperSize> projectPaperSizeOptional = projectPaperSizeRepository.findByCode(ePaperSize);

            if (projectPaperSizeOptional.isEmpty()) {
                ProjectRegistrationType projectRegistrationType = projectRegistrationTypeOptional.get();

                ProjectPaperSize projectPaperSize = new ProjectPaperSize()
                        .setProjectRegistrationType(projectRegistrationType)
                        .setCode(ePaperSize);
                projectPaperSizeRepository.save(projectPaperSize);

                ProjectRegistrationDate projectRegistrationDate = new ProjectRegistrationDate()
                        .setProjectPaperSize(projectPaperSize)
                        .setCode(registrationDateCode);
                projectRegistrationDateRepository.save(projectRegistrationDate);

                this.uploadCoverFileAndSave(
                        projectWard,
                        projectRegistrationType,
                        projectPaperSize,
                        projectRegistrationDate,
                        numberBookCode,
                        coverFile
                );
            }
            else {
                Optional<ProjectRegistrationDate> projectRegistrationDateOptional = projectRegistrationDateRepository.findByCode(registrationDateCode);

                if (projectRegistrationDateOptional.isEmpty()) {
                    ProjectRegistrationType projectRegistrationType = projectRegistrationTypeOptional.get();
                    ProjectPaperSize projectPaperSize = projectPaperSizeOptional.get();

                    ProjectRegistrationDate projectRegistrationDate = new ProjectRegistrationDate()
                            .setProjectPaperSize(projectPaperSize)
                            .setCode(registrationDateCode);
                    projectRegistrationDateRepository.save(projectRegistrationDate);

                    this.uploadCoverFileAndSave(
                            projectWard,
                            projectRegistrationType,
                            projectPaperSize,
                            projectRegistrationDate,
                            numberBookCode,
                            coverFile
                    );
                }
                else {
                    Optional<ProjectNumberBook> projectNumberBookOptional = projectNumberBookRepository.findByCodeAndStatusNot(numberBookCode, EProjectNumberBookStatus.CANCEL);

                    if (projectNumberBookOptional.isPresent()) {
                        String dateCode = projectRegistrationDateOptional.get().getCode();

                        if (projectNumberBookOptional.get().getStatus().equals(EProjectNumberBookStatus.NEW)) {
                            throw new DataInputException("Quyển sổ " + numberBookCode + " đăng ký năm " + dateCode + " đang chờ xét duyệt");
                        }

                        if (projectNumberBookOptional.get().getStatus().equals(EProjectNumberBookStatus.ACCEPT)) {
                            throw new DataInputException("Quyển sổ " + numberBookCode + " đã được sử dụng trong năm " + dateCode);
                        }
                    }
                    else {
                        ProjectRegistrationType projectRegistrationType = projectRegistrationTypeOptional.get();
                        ProjectPaperSize projectPaperSize = projectPaperSizeOptional.get();
                        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateOptional.get();

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
            }
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
                .setFileName(coverFileName)
                .setFolderPath(coverFolderPath)
                .setFileSize(fileSize);
        projectNumberBookCoverRepository.save(projectNumberBookCover);

        ProjectNumberBook projectNumberBook = new ProjectNumberBook()
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

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += parentsChildrenExtractShorts.size();

        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += parentsChildrenExtractFulls.size();

        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += birthExtractShorts.size();

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += birthExtractFulls.size();

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += marryExtractShorts.size();

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += marryExtractFulls.size();

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += wedlockExtractShorts.size();

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllByProjectAndImporterIsNull(project);
        totalCountExtractFull += wedlockExtractFulls.size();

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findByProjectAndImporterIsNull(project);
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

        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);
        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findByAccessPointAndStatusAndImporterIsNotNull(accessPoint, EInputStatus.IMPORTED);

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
