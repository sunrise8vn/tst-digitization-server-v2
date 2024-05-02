package com.tst.services.projectNumberBookFile;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookFile;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProjectNumberBookFileService extends IGeneralService<ProjectNumberBookFile, Long> {

    Optional<ProjectNumberBookFile> findByIdAndStatus(Long id, EProjectNumberBookFileStatus status);

    Optional<ProjectNumberBookFile> findByIdAndRegistrationTypeCodeAndStatus(Long id, String registrationTypeCode, EProjectNumberBookFileStatus status);

    List<String> create(List<MultipartFile> files, String folderPath, ProjectNumberBook projectNumberBook);

    List<String> uploadFilesAndSave(List<String> failedFiles, MultipartFile file, String folderPath, ProjectNumberBook projectNumberBook);

    ProjectNumberBookFile save(ProjectNumberBookFile projectNumberBookFile);

    void organization(ProjectNumberBookFile projectNumberBookFile, String dayMonthYear, String number) throws IOException;

    void approve(ProjectNumberBookFile projectNumberBookFile) throws IOException;
}
