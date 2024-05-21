package com.tst.services.projectNumberBook;

import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface IProjectNumberBookService extends IGeneralService<ProjectNumberBook, Long> {

    Optional<ProjectNumberBook> findByIdAndStatus(Long id, EProjectNumberBookStatus status);

    Optional<ProjectNumberBook> findByProjectAndId(Project project, Long id);

    ProjectNumberBook create(ProjectNumberBook projectNumberBook, MultipartFile coverFile);

    void updateAccept(ProjectNumberBook projectNumberBook);

    void updateCancel(ProjectNumberBook projectNumberBook);
}
