package com.tst.services.projectRegistrationType;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.repositories.ProjectPaperSizeRepository;
import com.tst.repositories.ProjectRegistrationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectRegistrationTypeService implements IProjectRegistrationTypeService {

    private final ProjectRegistrationTypeRepository projectRegistrationTypeRepository;
    private final ProjectPaperSizeRepository projectPaperSizeRepository;


    @Override
    public Optional<ProjectRegistrationType> findById(Long id) {
        return projectRegistrationTypeRepository.findById(id);
    }

    @Override
    public void updatePaperCountSize(ProjectRegistrationType projectRegistrationType) {
        PaperSizeDTO paperSizeDTO = projectPaperSizeRepository.findByProjectRegistrationType(projectRegistrationType);

        projectRegistrationType.setA0(paperSizeDTO.getA0());
        projectRegistrationType.setA1(paperSizeDTO.getA1());
        projectRegistrationType.setA2(paperSizeDTO.getA2());
        projectRegistrationType.setA3(paperSizeDTO.getA3());
        projectRegistrationType.setA4(paperSizeDTO.getA4());
        projectRegistrationType.setConvertA4(paperSizeDTO.getConvertA4());
        projectRegistrationType.setTotalSize(paperSizeDTO.getTotalSize());

        projectRegistrationTypeRepository.save(projectRegistrationType);
    }

    @Override
    public void delete(ProjectRegistrationType projectRegistrationType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
