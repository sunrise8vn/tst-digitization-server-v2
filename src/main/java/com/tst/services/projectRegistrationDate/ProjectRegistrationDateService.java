package com.tst.services.projectRegistrationDate;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.repositories.ProjectNumberBookRepository;
import com.tst.repositories.ProjectRegistrationDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectRegistrationDateService implements IProjectRegistrationDateService {

    private final ProjectRegistrationDateRepository projectRegistrationDateRepository;
    private final ProjectNumberBookRepository projectNumberBookRepository;


    @Override
    public Optional<ProjectRegistrationDate> findById(Long id) {
        return projectRegistrationDateRepository.findById(id);
    }

    @Override
    public void updatePaperCountSize(ProjectRegistrationDate projectRegistrationDate) {
        PaperSizeDTO paperSizeDTO = projectNumberBookRepository.findByProjectRegistrationDate(projectRegistrationDate);

        projectRegistrationDate.setA0(paperSizeDTO.getA0());
        projectRegistrationDate.setA1(paperSizeDTO.getA1());
        projectRegistrationDate.setA2(paperSizeDTO.getA2());
        projectRegistrationDate.setA3(paperSizeDTO.getA3());
        projectRegistrationDate.setA4(paperSizeDTO.getA4());
        projectRegistrationDate.setConvertA4(paperSizeDTO.getConvertA4());
        projectRegistrationDate.setTotalSize(paperSizeDTO.getTotalSize());

        projectRegistrationDateRepository.save(projectRegistrationDate);
    }

    @Override
    public void delete(ProjectRegistrationDate projectRegistrationDate) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
