package com.tst.services.projectDistrict;

import com.tst.models.dtos.project.PaperSizeDTO;
import com.tst.models.entities.ProjectDistrict;
import com.tst.models.entities.ProjectProvince;
import com.tst.models.responses.statistic.StatisticProjectDistrictResponse;
import com.tst.repositories.ProjectDistrictRepository;
import com.tst.repositories.ProjectWardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectDistrictService implements IProjectDistrictService {

    private final ProjectDistrictRepository projectDistrictRepository;
    private final ProjectWardRepository projectWardRepository;


    @Override
    public Optional<ProjectDistrict> findById(Long id) {
        return projectDistrictRepository.findById(id);
    }

    @Override
    public List<StatisticProjectDistrictResponse> findAllStatisticByProjectProvince(ProjectProvince projectProvince) {
        return projectDistrictRepository.findAllStatisticByProjectProvince(projectProvince);
    }

    @Override
    public void updatePaperCountSize(ProjectDistrict projectDistrict) {
        PaperSizeDTO paperSizeDTO = projectWardRepository.findByProjectDistrict(projectDistrict);

        projectDistrict.setA0(paperSizeDTO.getA0());
        projectDistrict.setA1(paperSizeDTO.getA1());
        projectDistrict.setA2(paperSizeDTO.getA2());
        projectDistrict.setA3(paperSizeDTO.getA3());
        projectDistrict.setA4(paperSizeDTO.getA4());
        projectDistrict.setConvertA4(paperSizeDTO.getConvertA4());
        projectDistrict.setTotalSize(paperSizeDTO.getTotalSize());

        projectDistrictRepository.save(projectDistrict);
    }

    @Override
    public void delete(ProjectDistrict projectDistrict) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
