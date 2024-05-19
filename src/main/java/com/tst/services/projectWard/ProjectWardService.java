package com.tst.services.projectWard;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectWard;
import com.tst.repositories.ProjectRegistrationTypeRepository;
import com.tst.repositories.ProjectWardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectWardService implements IProjectWardService {

    private final ProjectWardRepository projectWardRepository;
    private final ProjectRegistrationTypeRepository projectRegistrationTypeRepository;


    @Override
    public Optional<ProjectWard> findById(Long id) {
        return projectWardRepository.findById(id);
    }

    @Override
    public Optional<ProjectWard> findByIdAndProjectId(Long id, Long projectId) {
        return projectWardRepository.findByIdAndProjectId(id, projectId);
    }

    @Override
    public void updatePaperCountSize(ProjectWard projectWard) {
        PaperSizeDTO paperSizeDTO = projectRegistrationTypeRepository.findByProjectWard(projectWard);

        projectWard.setA0(paperSizeDTO.getA0());
        projectWard.setA1(paperSizeDTO.getA1());
        projectWard.setA2(paperSizeDTO.getA2());
        projectWard.setA3(paperSizeDTO.getA3());
        projectWard.setA4(paperSizeDTO.getA4());
        projectWard.setConvertA4(paperSizeDTO.getConvertA4());
        projectWard.setTotalSize(paperSizeDTO.getTotalSize());

        projectWardRepository.save(projectWard);
    }

    @Override
    public void delete(ProjectWard projectWard) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
