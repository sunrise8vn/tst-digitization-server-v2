package com.tst.services.project;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.*;
import com.tst.models.entities.extractFull.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.*;
import com.tst.repositories.extractShort.*;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


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
    public void delete(Project project) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
