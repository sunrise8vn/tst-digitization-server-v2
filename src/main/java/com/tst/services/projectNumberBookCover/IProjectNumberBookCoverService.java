package com.tst.services.projectNumberBookCover;

import com.tst.models.entities.ProjectNumberBookCover;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

public interface IProjectNumberBookCoverService extends IGeneralService<ProjectNumberBookCover, String> {

    ProjectNumberBookCover create(ProjectNumberBookCover projectNumberBookCover, MultipartFile coverFile);

    void uploadCoverFile(MultipartFile file, String fileName, String folderName);
}
