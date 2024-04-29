package com.tst.services.project;

import com.tst.exceptions.DataInputException;
import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.repositories.*;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectProvinceRepository projectProvinceRepository;
    private final ProjectDistrictRepository projectDistrictRepository;
    private final ProjectWardRepository projectWardRepository;
    private final ProjectRegistrationTypeRepository projectRegistrationTypeRepository;
    private final ProjectPaperSizeRepository projectPaperSizeRepository;
    private final ProjectRegistrationDateRepository projectRegistrationDateRepository;
    private final ProjectNumberBookRepository projectNumberBookRepository;
    private final ProjectNumberBookCoverRepository projectNumberBookCoverRepository;

    private final IProjectNumberBookCoverService projectNumberBookCoverService;


    @Value("${server.root-folder}")
    private String serverRootFolder;


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
                    .setRegistrationType(registrationType)
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
    public void delete(Project project) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
