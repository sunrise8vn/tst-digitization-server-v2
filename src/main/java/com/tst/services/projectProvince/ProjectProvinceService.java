package com.tst.services.projectProvince;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectProvince;
import com.tst.models.responses.statistic.StatisticProjectProvinceResponse;
import com.tst.repositories.ProjectDistrictRepository;
import com.tst.repositories.ProjectProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectProvinceService implements IProjectProvinceService {
    private final ProjectProvinceRepository projectProvinceRepository;
    private final ProjectDistrictRepository projectDistrictRepository;


    @Override
    public Optional<ProjectProvince> findById(Long id) {
        return projectProvinceRepository.findById(id);
    }

    @Override
    public List<StatisticProjectProvinceResponse> findAllStatisticByProject(Project project) {
        return projectProvinceRepository.findAllStatisticByProject(project);
    }

    @Override
    public void updatePaperCountSize(ProjectProvince projectProvince) {
        PaperSizeDTO paperSizeDTO = projectDistrictRepository.findByProjectProvince(projectProvince);

        projectProvince.setA0(paperSizeDTO.getA0());
        projectProvince.setA1(paperSizeDTO.getA1());
        projectProvince.setA2(paperSizeDTO.getA2());
        projectProvince.setA3(paperSizeDTO.getA3());
        projectProvince.setA4(paperSizeDTO.getA4());
        projectProvince.setConvertA4(paperSizeDTO.getConvertA4());
        projectProvince.setTotalSize(paperSizeDTO.getTotalSize());

        projectProvinceRepository.save(projectProvince);
    }

    @Override
    public void delete(ProjectProvince projectProvince) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
