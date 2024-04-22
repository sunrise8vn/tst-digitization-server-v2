package com.tst.services.projectProvince;

import com.tst.models.entities.ProjectProvince;
import com.tst.repositories.ProjectProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProjectProvinceService implements IProjectProvinceService {
    private final ProjectProvinceRepository projectProvinceRepository;


    @Override
    public Optional<ProjectProvince> findById(Long id) {
        return projectProvinceRepository.findById(id);
    }

    @Override
    public void delete(ProjectProvince projectProvince) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
