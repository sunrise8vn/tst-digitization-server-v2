package com.tst.services.projectPaperSize;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectPaperSize;
import com.tst.models.entities.ProjectRegistrationType;
import com.tst.models.responses.project.ProjectPaperSizeResponse;
import com.tst.models.responses.statistic.StatisticProjectNumberBookResponse;
import com.tst.models.responses.statistic.StatisticProjectPaperSizeResponse;
import com.tst.repositories.ProjectPaperSizeRepository;
import com.tst.repositories.ProjectRegistrationDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectPaperSizeService implements IProjectPaperSizeService {

    private final ProjectPaperSizeRepository projectPaperSizeRepository;
    private final ProjectRegistrationDateRepository projectRegistrationDateRepository;


    @Override
    public Optional<ProjectPaperSize> findById(Long id) {
        return projectPaperSizeRepository.findById(id);
    }

    @Override
    public List<ProjectPaperSizeResponse> findAllByProjectRegistrationType(ProjectRegistrationType projectRegistrationType) {
        return projectPaperSizeRepository.findAllByProjectRegistrationType(projectRegistrationType);
    }

    @Override
    public List<StatisticProjectPaperSizeResponse> findAllStatisticByProjectRegistrationType(ProjectRegistrationType projectRegistrationType) {
        return projectPaperSizeRepository.findAllStatisticByProjectRegistrationType(projectRegistrationType);
    }

    @Override
    public void updatePaperCountSize(ProjectPaperSize projectPaperSize) {
        PaperSizeDTO paperSizeDTO = projectRegistrationDateRepository.findByProjectPaperSize(projectPaperSize);

        projectPaperSize.setA0(paperSizeDTO.getA0());
        projectPaperSize.setA1(paperSizeDTO.getA1());
        projectPaperSize.setA2(paperSizeDTO.getA2());
        projectPaperSize.setA3(paperSizeDTO.getA3());
        projectPaperSize.setA4(paperSizeDTO.getA4());
        projectPaperSize.setConvertA4(paperSizeDTO.getConvertA4());
        projectPaperSize.setTotalSize(paperSizeDTO.getTotalSize());

        projectPaperSizeRepository.save(projectPaperSize);
    }

    @Override
    public void delete(ProjectPaperSize projectPaperSize) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
