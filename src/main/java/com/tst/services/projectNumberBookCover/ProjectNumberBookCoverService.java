package com.tst.services.projectNumberBookCover;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.ProjectNumberBookCover;
import com.tst.repositories.ProjectNumberBookCoverRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectNumberBookCoverService implements IProjectNumberBookCoverService {


    private final ResourceLoader resourceLoader;
    private final ProjectNumberBookCoverRepository projectNumberBookCoverRepository;

    @Value("${server.root-folder}")
    private String serverRootFolder;

    @Override
    public Optional<ProjectNumberBookCover> findById(String id) {
        return projectNumberBookCoverRepository.findById(id);
    }

    @Override
    public ProjectNumberBookCover create(ProjectNumberBookCover projectNumberBookCover, MultipartFile coverFile) {
        this.uploadCoverFile(coverFile, projectNumberBookCover.getFileName(), projectNumberBookCover.getFolderName());

        return projectNumberBookCoverRepository.save(projectNumberBookCover);
    }

    @Override
    public void uploadCoverFile(MultipartFile file, String fileName, String folderName) {
        try {
            Path uploadPath = Paths.get(serverRootFolder + "/" + folderName);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path destinationFile = Paths.get(uploadPath + "/" + fileName);

            if (!Files.exists(destinationFile)) {
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                throw new DataInputException("Tập tin đã tồn tại");
            }
        }
        catch (Exception ex) {
            throw new DataInputException("Không thể lưu trữ tệp. " + ex.getMessage());
        }
    }

    @Override
    public void deleteCoverDirectory(ProjectNumberBookCover projectNumberBookCover) throws IOException {
        Path destinationPath = Paths.get(serverRootFolder + "/" + projectNumberBookCover.getFolderName());

        FileUtils.deleteDirectory(destinationPath.toFile());
    }

    @Override
    public void delete(ProjectNumberBookCover projectNumberBookCover) {

    }

    @Override
    public void deleteById(String id) {

    }
}
