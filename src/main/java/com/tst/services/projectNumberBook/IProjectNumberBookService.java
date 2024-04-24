package com.tst.services.projectNumberBook;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

public interface IProjectNumberBookService extends IGeneralService<ProjectNumberBook, Long> {

    ProjectNumberBook create(ProjectNumberBook projectNumberBook, MultipartFile coverFile);
}
