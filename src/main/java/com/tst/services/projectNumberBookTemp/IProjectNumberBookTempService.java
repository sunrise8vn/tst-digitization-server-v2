package com.tst.services.projectNumberBookTemp;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookTemp;
import com.tst.models.enums.EProjectNumberBookTempStatus;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProjectNumberBookTempService extends IGeneralService<ProjectNumberBookTemp, Long> {

    Optional<ProjectNumberBookTemp> findByIdAndStatus(Long id, EProjectNumberBookTempStatus status);

    Optional<ProjectNumberBookTemp> findByIdAndRegistrationTypeCodeAndStatus(Long id, String registrationTypeCode, EProjectNumberBookTempStatus status);

    List<String> create(List<MultipartFile> files, String folderPath, ProjectNumberBook projectNumberBook);

    List<String> uploadFilesAndSave(List<String> failedFiles, MultipartFile file, String folderPath, ProjectNumberBook projectNumberBook);

    ProjectNumberBookTemp save(ProjectNumberBookTemp projectNumberBookTemp);

    void organization(ProjectNumberBookTemp projectNumberBookTemp, String dayMonthYear, String number) throws IOException;

    void approve(ProjectNumberBookTemp projectNumberBookTemp) throws IOException;
}
