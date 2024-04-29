package com.tst.services.projectNumberBookTemp;

import com.tst.TstDigitizationServerApplication;
import com.tst.exceptions.DataInputException;
import com.tst.models.entities.*;
import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookTempStatus;
import com.tst.repositories.*;
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
public class ProjectNumberBookTempService implements IProjectNumberBookTempService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectNumberBookTempService.class);

    private final ProjectNumberBookRepository projectNumberBookRepository;
    private final ProjectNumberBookTempRepository projectNumberBookTempRepository;

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
    public Optional<ProjectNumberBookTemp> findById(Long id) {
        return projectNumberBookTempRepository.findById(id);
    }

    @Override
    public Optional<ProjectNumberBookTemp> findByIdAndStatus(Long id, EProjectNumberBookTempStatus status) {
        return projectNumberBookTempRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<ProjectNumberBookTemp> findByIdAndRegistrationTypeCodeAndStatus(Long id, String registrationTypeCode, EProjectNumberBookTempStatus status) {
        return projectNumberBookTempRepository.findByIdAndRegistrationTypeCodeAndStatus(id, registrationTypeCode, status);
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
    public List<String> uploadFilesAndSave(List<String> failedFiles, MultipartFile file, String folderPath, ProjectNumberBook projectNumberBook) {
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

                    ProjectNumberBookTemp projectNumberBookTemp = new ProjectNumberBookTemp();
                    projectNumberBookTemp.setFileName(fileName);
                    projectNumberBookTemp.setFolderPath(folderPath);
                    EPaperSize paperSize = projectNumberBook.getProjectRegistrationDate().getProjectPaperSize().getCode();
                    projectNumberBookTemp.setPaperSize(paperSize);
                    projectNumberBookTemp.setFileSize(fileSize);
                    projectNumberBookTemp.setProjectNumberBook(projectNumberBook);
                    projectNumberBookTemp.setStatus(EProjectNumberBookTempStatus.NEW);

                    projectNumberBookTempRepository.save(projectNumberBookTemp);

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
    public ProjectNumberBookTemp save(ProjectNumberBookTemp projectNumberBookTemp) {
        return projectNumberBookTempRepository.save(projectNumberBookTemp);
    }


    @Override
    // IOException là checked exception, nó không kích hoạt việc rollback nên cần chỉ định cho transaction.
    @Transactional(rollbackFor = IOException.class)
    public void organization(ProjectNumberBookTemp projectNumberBookTemp, String dayMonthYear, String number) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!appUtils.isValidDateDot(dayMonthYear)) {
            throw new DataInputException("Ngày tháng năm không hợp lệ");
        }

        dayMonthYear = appUtils.convertDayMonthYearDotToYearMonthDayKebab(dayMonthYear);

        // Định dạng lại chuỗi để có độ dài là 3 ký tự, thêm các số 0 vào đầu nếu cần
        number = String.format("%03d", Integer.parseInt(number));

        String registrationType = projectNumberBookTemp.getProjectNumberBook().getProjectRegistrationDate().getProjectPaperSize().getProjectRegistrationType().getCode();
        String registrationDate = projectNumberBookTemp.getProjectNumberBook().getProjectRegistrationDate().getCode();
        String numberBook = projectNumberBookTemp.getProjectNumberBook().getCode();

        String folderPath = projectNumberBookTemp.getFolderPath();
        String oldFileName = projectNumberBookTemp.getFileName();
        String sourceFile = serverRootFolder + "/" + folderPath + "/" + oldFileName;

        String newFileName = registrationType + "." +
                registrationDate + "." +
                numberBook + "." +
                dayMonthYear + "." +
                number + ".pdf";

        boolean existFileName = projectNumberBookTempRepository.existsByFileNameAndProjectNumberBook(
                newFileName,
                projectNumberBookTemp.getProjectNumberBook()
        );

        if (existFileName) {
            throw new DataInputException("Tên tập tin đã tồn tại");
        }

        projectNumberBookTemp.setFileName(newFileName);
        projectNumberBookTemp.setOrganizedBy(user);
        projectNumberBookTemp.setStatus(EProjectNumberBookTempStatus.ORGANIZED);

        projectNumberBookTempRepository.save(projectNumberBookTemp);

        fileUtils.renameFile(sourceFile, newFileName);
    }

    @Override
    // IOException là checked exception, nó không kích hoạt việc rollback nên cần chỉ định cho transaction.
    @Transactional(rollbackFor = IOException.class)
    public void approve(ProjectNumberBookTemp projectNumberBookTemp) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String oldFolderPath = projectNumberBookTemp.getFolderPath();
        String fileName = projectNumberBookTemp.getFileName();
        String sourceFile = serverRootFolder + "/" + oldFolderPath + "/" + fileName;

        String newFolderPath = fileUtils.moveFileUpOneDirectory(sourceFile);

        newFolderPath = newFolderPath.replace("storage\\", "");
        newFolderPath = newFolderPath.replace("\\", "/");

        projectNumberBookTemp.setFolderPath(newFolderPath);
        projectNumberBookTemp.setApprovedBy(user);
        projectNumberBookTemp.setStatus(EProjectNumberBookTempStatus.ACCEPT);
        projectNumberBookTempRepository.save(projectNumberBookTemp);

        ProjectNumberBook projectNumberBook = projectNumberBookTemp.getProjectNumberBook();

        EPaperSize paperSize = projectNumberBook.getProjectRegistrationDate().getProjectPaperSize().getCode();

        // START cập nhật tổng số trang và dung lượng cho ProjectNumberBook

        long countProjectNumberBook = projectNumberBookTempRepository.countByProjectNumberBookAndStatus(
                projectNumberBook,
                EProjectNumberBookTempStatus.ACCEPT
        );

        long totalFileSizeProjectNumberBook = projectNumberBookTempRepository.findTotalFileSizeByProjectNumberBookAndStatus(
                projectNumberBook,
                EProjectNumberBookTempStatus.ACCEPT
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

        Project project = projectProvince.getProject();
        projectService.updatePaperCountSize(project);

        Path tempFolderPath = Path.of(serverRootFolder + "/" + oldFolderPath);

        if (fileUtils.isDirectoryEmpty(tempFolderPath)) {
            Files.delete(tempFolderPath);
            logger.info("\uD83D\uDCC1 Thư mục " + tempFolderPath + " đã được xóa vì nó trống.");
        }
    }

    @Override
    public void delete(ProjectNumberBookTemp projectNumberBookTemp) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
