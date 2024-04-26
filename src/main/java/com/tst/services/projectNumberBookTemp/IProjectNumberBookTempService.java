package com.tst.services.projectNumberBookTemp;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectNumberBookTemp;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProjectNumberBookTempService extends IGeneralService<ProjectNumberBookTemp, Long> {

    List<String> create(List<MultipartFile> files, String folderName, ProjectNumberBook projectNumberBook);

    List<String> uploadFilesAndSave(List<String> failedFiles, MultipartFile file, String folderName, ProjectNumberBook projectNumberBook);
}
