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
import com.tst.models.enums.EAccessPointStatus;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.*;
import com.tst.repositories.extractShort.*;
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


    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
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

        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += birthExtractFulls.size();

        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += marryExtractShorts.size();

        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += marryExtractFulls.size();

        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += wedlockExtractShorts.size();

        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += wedlockExtractFulls.size();

        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractShort += deathExtractShorts.size();

        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findByProjectAndImporterIsNull(project);
        totalCountExtractFull += deathExtractFulls.size();

        long totalCountExtractForm = totalCountExtractShort + totalCountExtractFull;

        if (totalCount > totalCountExtractForm) {
            throw new DataInputException("Tổng số phiếu cần nhập chỉ còn " + totalCountExtractForm + " phiếu");
        }

        long countExtractShort = 0L;
        long countExtractFull = 0L;

        // Khởi tạo AccessPoint để lưu vào các biểu mẫu
        AccessPoint accessPoint = new AccessPoint()
                .setProject(project)
                .setStatus(EAccessPointStatus.PROCESSING)
                .setCountExtractShort(countExtractShort)
                .setCountExtractFull(countExtractFull)
                .setTotalCount(totalCount);
        accessPointRepository.save(accessPoint);

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

        int userIndex = 0; // Index của người dùng hiện tại
        long formsToAssign = countExtractPerUser[userIndex]; // Số lượng biểu mẫu cần phân phối cho người dùng hiện tại

        // Phân phối ParentsChildrenExtractShort
        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                parentsChildrenExtractShortRepository.save(item);
                formsToAssign--;
                countExtractShortPerUser[userIndex]++;
                countExtractShort++;
            }
        }

        // Phân phối ParentsChildrenExtractFull
        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                parentsChildrenExtractFullRepository.save(item);
                formsToAssign--;
                countExtractFullPerUser[userIndex]++;
                countExtractFull++;
            }
        }

        // Phân phối BirthExtractShort
        for (BirthExtractShort item : birthExtractShorts) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                birthExtractShortRepository.save(item);
                formsToAssign--;
                countExtractShortPerUser[userIndex]++;
                countExtractShort++;
            }
        }

        // Phân phối BirthExtractFull
        for (BirthExtractFull item : birthExtractFulls) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                birthExtractFullRepository.save(item);
                formsToAssign--;
                countExtractFullPerUser[userIndex]++;
                countExtractFull++;
            }
        }

        // Phân phối MarryExtractShort
        for (MarryExtractShort item : marryExtractShorts) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                marryExtractShortRepository.save(item);
                formsToAssign--;
                countExtractShortPerUser[userIndex]++;
                countExtractShort++;
            }
        }

        // Phân phối MarryExtractFull
        for (MarryExtractFull item : marryExtractFulls) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                marryExtractFullRepository.save(item);
                formsToAssign--;
                countExtractFullPerUser[userIndex]++;
                countExtractFull++;
            }
        }

        // Phân phối WedlockExtractShort
        for (WedlockExtractShort item : wedlockExtractShorts) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                wedlockExtractShortRepository.save(item);
                formsToAssign--;
                countExtractShortPerUser[userIndex]++;
                countExtractShort++;
            }
        }

        // Phân phối WedlockExtractFull
        for (WedlockExtractFull item : wedlockExtractFulls) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                wedlockExtractFullRepository.save(item);
                formsToAssign--;
                countExtractFullPerUser[userIndex]++;
                countExtractFull++;
            }
        }

        // Phân phối DeathExtractShort
        for (DeathExtractShort item : deathExtractShorts) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                deathExtractShortRepository.save(item);
                formsToAssign--;
                countExtractShortPerUser[userIndex]++;
                countExtractShort++;
            }
        }

        // Phân phối DeathExtractFull
        for (DeathExtractFull item : deathExtractFulls) {
            if (formsToAssign == 0 && userIndex + 1 < totalUsers) {
                userIndex++;
                formsToAssign = countExtractPerUser[userIndex];
            }

            if (formsToAssign > 0) {
                item.setAccessPoint(accessPoint);
                item.setImporter(users.get(userIndex));
                deathExtractFullRepository.save(item);
                formsToAssign--;
                countExtractFullPerUser[userIndex]++;
                countExtractFull++;
            }
        }

        // Cập nhật số lượng các biểu mẫu được phân phối cho AccessPoint
        accessPoint.setCountExtractShort(countExtractShort);
        accessPoint.setCountExtractFull(countExtractFull);
        accessPointRepository.save(accessPoint);

        for (int i = 0; i < totalUsers; i++) {
            AccessPointHistory accessPointHistory = new AccessPointHistory()
                    .setAccessPoint(accessPoint)
                    .setProject(project)
                    .setCountExtractShort(countExtractShortPerUser[i])
                    .setCountExtractFull(countExtractFullPerUser[i])
                    .setAssignees(users.get(i))
                    .setTotalCount(countExtractPerUser[i]);
            accessPointHistoryRepository.save(accessPointHistory);
        }

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
            parentsChildrenExtractShortRepository.saveAll(parentsChildrenExtractShortsModified);
        }

        if (!parentsChildrenExtractFullsModified.isEmpty()) {
            parentsChildrenExtractFullRepository.saveAll(parentsChildrenExtractFullsModified);
        }

        if (!birthExtractShortsModified.isEmpty()) {
            birthExtractShortRepository.saveAll(birthExtractShortsModified);
        }

        if (!birthExtractFullsModified.isEmpty()) {
            birthExtractFullRepository.saveAll(birthExtractFullsModified);
        }

        if (!marryExtractShortsModified.isEmpty()) {
            marryExtractShortRepository.saveAll(marryExtractShortsModified);
        }

        if (!marryExtractFullsModified.isEmpty()) {
            marryExtractFullRepository.saveAll(marryExtractFullsModified);
        }

        if (!wedlockExtractShortsModified.isEmpty()) {
            wedlockExtractShortRepository.saveAll(wedlockExtractShortsModified);
        }

        if (!wedlockExtractFullsModified.isEmpty()) {
            wedlockExtractFullRepository.saveAll(wedlockExtractFullsModified);
        }

        if (!deathExtractShortsModified.isEmpty()) {
            deathExtractShortRepository.saveAll(deathExtractShortsModified);
        }

        if (!deathExtractFullsModified.isEmpty()) {
            deathExtractFullRepository.saveAll(deathExtractFullsModified);
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
