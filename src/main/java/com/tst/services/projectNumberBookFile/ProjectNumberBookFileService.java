package com.tst.services.projectNumberBookFile;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.*;
import com.tst.models.entities.extractFull.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.enums.EInputStatus;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.enums.ERegistrationType;
import com.tst.models.responses.project.NumberBookFileListResponse;
import com.tst.repositories.*;
import com.tst.repositories.extractFull.*;
import com.tst.repositories.extractShort.*;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectPaperSize.IProjectPaperSizeService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectRegistrationDate.IProjectRegistrationDateService;
import com.tst.services.projectRegistrationType.IProjectRegistrationTypeService;
import com.tst.services.projectWard.IProjectWardService;
import com.tst.utils.AppUtils;
import com.tst.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectNumberBookFileService implements IProjectNumberBookFileService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectNumberBookFileService.class);

    private final ProjectNumberBookRepository projectNumberBookRepository;
    private final ProjectNumberBookFileRepository projectNumberBookFileRepository;
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

    private final IProjectService projectService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectWardService projectWardService;
    private final IProjectRegistrationTypeService projectRegistrationTypeService;
    private final IProjectPaperSizeService projectPaperSizeService;
    private final IProjectRegistrationDateService projectRegistrationDateService;

    private final AppUtils appUtils;
    private final FileUtils fileUtils;


    @Value("${server.root-folder}")
    private String serverRootFolder;


    @Override
    public Optional<ProjectNumberBookFile> findById(Long id) {
        return projectNumberBookFileRepository.findById(id);
    }

    @Override
    public Optional<ProjectNumberBookFile> findByIdAndStatus(Long id, EProjectNumberBookFileStatus status) {
        return projectNumberBookFileRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<ProjectNumberBookFile> findByIdAndRegistrationTypeCodeAndStatus(
            Long id,
            String registrationTypeCode,
            EProjectNumberBookFileStatus status
    ) {
        return projectNumberBookFileRepository.findByIdAndRegistrationTypeCodeAndStatus(id, registrationTypeCode, status);
    }

    @Override
    public List<NumberBookFileListResponse> findAllNumberBookFileByStatus(
            ProjectWard projectWard,
            EProjectNumberBookFileStatus status
    ) {
        return projectNumberBookFileRepository.findAllNumberBookFileByStatus(projectWard, status);
    }

    @Override
    public List<String> create(List<MultipartFile> files, String folderPath, ProjectNumberBook projectNumberBook) {
        List<String> failedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            failedFiles = this.uploadFilesAndSave(failedFiles, file, folderPath, projectNumberBook);
        }

        return failedFiles;
    }

    @Override
    public List<String> uploadFilesAndSave(
            List<String> failedFiles,
            MultipartFile file,
            String folderPath,
            ProjectNumberBook projectNumberBook
    ) {
        try {
            folderPath = folderPath + "/temp";

            Path uploadPath = Paths.get(serverRootFolder + "/" + folderPath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = appUtils.removePdfTypeChar(file.getOriginalFilename());
            fileName = appUtils.replaceNonEnglishChar(fileName);
            fileName = appUtils.removeNonAlphanumeric(fileName);
            fileName = fileName + ".pdf";

            Path destinationFile = Paths.get(uploadPath + "/" + fileName);

            if (!Files.exists(destinationFile)) {
                try (InputStream inputStream = file.getInputStream()) {
                    Long fileSize = file.getSize() / 1024; // KB

                    EPaperSize paperSize = projectNumberBook.getProjectRegistrationDate()
                            .getProjectPaperSize()
                            .getCode();

                    ERegistrationType registrationType = projectNumberBook.getProjectRegistrationDate()
                            .getProjectPaperSize()
                            .getProjectRegistrationType()
                            .getCode();

                    ProjectNumberBookFile projectNumberBookFile = new ProjectNumberBookFile()
                            .setFileName(fileName)
                            .setFolderPath(folderPath)
                            .setPaperSize(paperSize)
                            .setRegistrationType(registrationType)
                            .setFileSize(fileSize)
                            .setProjectNumberBook(projectNumberBook)
                            .setProject(projectNumberBook.getProject())
                            .setStatus(EProjectNumberBookFileStatus.NEW);

                    projectNumberBookFileRepository.save(projectNumberBookFile);

                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    failedFiles.add(file.getOriginalFilename());
                }
            } else {
                failedFiles.add(file.getOriginalFilename());
            }
        } catch (Exception ex) {
            failedFiles.add(file.getOriginalFilename());
        }

        return failedFiles;
    }

    @Override
    public ProjectNumberBookFile save(ProjectNumberBookFile projectNumberBookFile) {
        return projectNumberBookFileRepository.save(projectNumberBookFile);
    }

    @Override
    // IOException là checked exception, nó không kích hoạt việc rollback nên cần chỉ định cho transaction.
    @Transactional(rollbackFor = IOException.class)
    public void organization(
            ProjectNumberBookFile projectNumberBookFile,
            String dayMonthYear,
            String number
    ) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!appUtils.isValidDateDot(dayMonthYear)) {
            throw new DataInputException("Ngày tháng năm không hợp lệ");
        }

        dayMonthYear = appUtils.convertDayMonthYearDotToYearMonthDayKebab(dayMonthYear);

        // Định dạng lại chuỗi để có độ dài là 3 ký tự, thêm các số 0 vào đầu nếu cần
        number = String.format("%03d", Integer.parseInt(number));

        String registrationType = projectNumberBookFile.getProjectNumberBook()
                .getProjectRegistrationDate()
                .getProjectPaperSize()
                .getProjectRegistrationType()
                .getCode()
                .getValue();

        String registrationDate = projectNumberBookFile.getProjectNumberBook()
                .getProjectRegistrationDate()
                .getCode();

        String numberBook = projectNumberBookFile.getProjectNumberBook().getCode();

        String folderPath = projectNumberBookFile.getFolderPath();
        String oldFileName = projectNumberBookFile.getFileName();
        String sourceFile = serverRootFolder + "/" + folderPath + "/" + oldFileName;

        String newFileName = registrationType + "." +
                registrationDate + "." +
                numberBook + "." +
                dayMonthYear + "." +
                number + ".pdf";

        boolean existFileName = projectNumberBookFileRepository.existsByFileNameAndProjectNumberBook(
                newFileName,
                projectNumberBookFile.getProjectNumberBook()
        );

        if (existFileName) {
            throw new DataInputException("Tên tập tin đã tồn tại");
        }

        projectNumberBookFile.setFileName(newFileName);
        projectNumberBookFile.setOrganizedBy(user);
        projectNumberBookFile.setStatus(EProjectNumberBookFileStatus.ORGANIZED);

        projectNumberBookFileRepository.save(projectNumberBookFile);

        fileUtils.renameFile(sourceFile, newFileName);
    }

    @Override
    // IOException là checked exception, nó không kích hoạt việc rollback nên cần chỉ định cho transaction.
    @Transactional(rollbackFor = IOException.class)
    public void approve(ProjectNumberBookFile projectNumberBookFile) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String oldFolderPath = projectNumberBookFile.getFolderPath();
        String fileName = projectNumberBookFile.getFileName();
        String sourceFile = serverRootFolder + "/" + oldFolderPath + "/" + fileName;

        String newFolderPath = fileUtils.moveFileUpOneDirectory(sourceFile);

        newFolderPath = newFolderPath.replace("storage\\", "");
        newFolderPath = newFolderPath.replace("\\", "/");

        projectNumberBookFile.setFolderPath(newFolderPath);
        projectNumberBookFile.setApprovedBy(user);
        projectNumberBookFile.setStatus(EProjectNumberBookFileStatus.ACCEPT);
        projectNumberBookFileRepository.save(projectNumberBookFile);

        Project project = projectNumberBookFile.getProjectNumberBook()
                .getProjectRegistrationDate()
                .getProjectPaperSize()
                .getProjectRegistrationType()
                .getProjectWard()
                .getProjectDistrict()
                .getProjectProvince()
                .getProject();

        if (projectNumberBookFile.getRegistrationType().equals(ERegistrationType.CMC)) {
            ParentsChildrenExtractShort parentsChildrenExtractShort = new ParentsChildrenExtractShort()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            parentsChildrenExtractShortRepository.save(parentsChildrenExtractShort);

            ParentsChildrenExtractFull parentsChildrenExtractFull = new ParentsChildrenExtractFull()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            parentsChildrenExtractFullRepository.save(parentsChildrenExtractFull);
        }

        if (projectNumberBookFile.getRegistrationType().equals(ERegistrationType.KS)) {
            BirthExtractShort birthExtractShort = new BirthExtractShort()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            birthExtractShortRepository.save(birthExtractShort);

            BirthExtractFull birthExtractFull = new BirthExtractFull()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            birthExtractFullRepository.save(birthExtractFull);
        }

        if (projectNumberBookFile.getRegistrationType().equals(ERegistrationType.KH)) {
            MarryExtractShort marryExtractShort = new MarryExtractShort()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            marryExtractShortRepository.save(marryExtractShort);

            MarryExtractFull marryExtractFull = new MarryExtractFull()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            marryExtractFullRepository.save(marryExtractFull);
        }

        if (projectNumberBookFile.getRegistrationType().equals(ERegistrationType.HN)) {
            WedlockExtractShort wedlockExtractShort = new WedlockExtractShort()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            wedlockExtractShortRepository.save(wedlockExtractShort);

            WedlockExtractFull wedlockExtractFull = new WedlockExtractFull()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            wedlockExtractFullRepository.save(wedlockExtractFull);
        }

        if (projectNumberBookFile.getRegistrationType().equals(ERegistrationType.KT)) {
            DeathExtractShort deathExtractShort = new DeathExtractShort()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            deathExtractShortRepository.save(deathExtractShort);

            DeathExtractFull deathExtractFull = new DeathExtractFull()
                    .setProject(project)
                    .setProjectNumberBookFile(projectNumberBookFile)
                    .setStatus(EInputStatus.NEW);
            deathExtractFullRepository.save(deathExtractFull);
        }

        ProjectNumberBook projectNumberBook = projectNumberBookFile.getProjectNumberBook();

        EPaperSize paperSize = projectNumberBook.getProjectRegistrationDate().getProjectPaperSize().getCode();

        // START cập nhật tổng số trang và dung lượng cho ProjectNumberBook

        long countProjectNumberBook = projectNumberBookFileRepository.countByProjectNumberBookAndStatus(
                projectNumberBook,
                EProjectNumberBookFileStatus.ACCEPT
        );

        long totalFileSizeProjectNumberBook = projectNumberBookFileRepository.findTotalFileSizeByProjectNumberBookAndStatus(
                projectNumberBook,
                EProjectNumberBookFileStatus.ACCEPT
        );

        long countProjectNumberBookA0 = 0L;
        long countProjectNumberBookA1 = 0L;
        long countProjectNumberBookA2 = 0L;
        long countProjectNumberBookA3 = 0L;
        long countProjectNumberBookA4 = 0L;

        if (paperSize.equals(EPaperSize.A0)) {
            countProjectNumberBookA0 = countProjectNumberBook;
        }

        if (paperSize.equals(EPaperSize.A1)) {
            countProjectNumberBookA1 = countProjectNumberBook;
        }

        if (paperSize.equals(EPaperSize.A2)) {
            countProjectNumberBookA2 = countProjectNumberBook;
        }

        if (paperSize.equals(EPaperSize.A3)) {
            countProjectNumberBookA3 = countProjectNumberBook;
        }

        if (paperSize.equals(EPaperSize.A4)) {
            countProjectNumberBookA4 = countProjectNumberBook;
        }

        long convertProjectNumberBookA4 = countProjectNumberBookA0 * 16 +
                countProjectNumberBookA1 * 8 +
                countProjectNumberBookA2 * 4 +
                countProjectNumberBookA3 * 2 +
                countProjectNumberBookA4;

        projectNumberBook.setA0(countProjectNumberBookA0);
        projectNumberBook.setA1(countProjectNumberBookA1);
        projectNumberBook.setA2(countProjectNumberBookA2);
        projectNumberBook.setA3(countProjectNumberBookA3);
        projectNumberBook.setA4(countProjectNumberBookA4);
        projectNumberBook.setConvertA4(convertProjectNumberBookA4);
        projectNumberBook.setTotalSize(totalFileSizeProjectNumberBook);
        projectNumberBookRepository.save(projectNumberBook);

        // END cập nhật tổng số trang và dung lượng cho ProjectNumberBook

        ProjectRegistrationDate projectRegistrationDate = projectNumberBook.getProjectRegistrationDate();
        projectRegistrationDateService.updatePaperCountSize(projectRegistrationDate);

        ProjectPaperSize projectPaperSize = projectRegistrationDate.getProjectPaperSize();
        projectPaperSizeService.updatePaperCountSize(projectPaperSize);

        ProjectRegistrationType projectRegistrationType = projectPaperSize.getProjectRegistrationType();
        projectRegistrationTypeService.updatePaperCountSize(projectRegistrationType);

        ProjectWard projectWard = projectRegistrationType.getProjectWard();
        projectWardService.updatePaperCountSize(projectWard);

        ProjectDistrict projectDistrict = projectWard.getProjectDistrict();
        projectDistrictService.updatePaperCountSize(projectDistrict);

        ProjectProvince projectProvince = projectDistrict.getProjectProvince();
        projectProvinceService.updatePaperCountSize(projectProvince);

        projectService.updatePaperCountSize(project);

        Path tempFolderPath = Path.of(serverRootFolder + "/" + oldFolderPath);

        if (fileUtils.isDirectoryEmpty(tempFolderPath)) {
            Files.delete(tempFolderPath);
            logger.info("\uD83D\uDCC1 Thư mục " + tempFolderPath + " đã được xóa vì nó trống.");
        }
    }

    @Override
    public void delete(ProjectNumberBookFile projectNumberBookFile) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
