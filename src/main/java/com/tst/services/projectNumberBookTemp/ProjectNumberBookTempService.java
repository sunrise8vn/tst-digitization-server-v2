package com.tst.services.projectNumberBookTemp;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookTemp;
import com.tst.repositories.ProjectNumberBookTempRepository;
import com.tst.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    private final ProjectNumberBookTempRepository projectNumberBookTempRepository;

    private final AppUtils appUtils;

    @Value("${server.root-folder}")
    private String serverRootFolder;


    @Override
    public Optional<ProjectNumberBookTemp> findById(Long id) {
        return projectNumberBookTempRepository.findById(id);
    }

    @Override
    public List<String> create(List<MultipartFile> files, String folderName, ProjectNumberBook projectNumberBook) {
        List<String> failedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            failedFiles = this.uploadFilesAndSave(failedFiles, file, folderName, projectNumberBook);
        }

        return failedFiles;
    }

    @Override
    public List<String> uploadFilesAndSave(List<String> failedFiles, MultipartFile file, String folderName, ProjectNumberBook projectNumberBook) {
        try {
            folderName = folderName + "/temp";

            Path uploadPath = Paths.get(serverRootFolder + "/" + folderName);

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
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                    Long fileSize = file.getSize() / 1024; // KB

                    ProjectNumberBookTemp projectNumberBookTemp = new ProjectNumberBookTemp();
                    projectNumberBookTemp.setFileName(fileName);
                    projectNumberBookTemp.setFolderName(folderName);
                    projectNumberBookTemp.setFileSize(fileSize);
                    projectNumberBookTemp.setSizeType("KB");
                    projectNumberBookTemp.setProjectNumberBook(projectNumberBook);

                    projectNumberBookTempRepository.save(projectNumberBookTemp);
                } catch (IOException e) {
                    failedFiles.add(file.getOriginalFilename());
                }
            }
            else {
                failedFiles.add(file.getOriginalFilename());
            }
        }
        catch (Exception ex) {
            failedFiles.add(file.getOriginalFilename());
        }

        return failedFiles;
    }

    @Override
    public void delete(ProjectNumberBookTemp projectNumberBookTemp) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
