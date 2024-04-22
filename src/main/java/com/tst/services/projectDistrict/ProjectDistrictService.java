package com.tst.services.projectDistrict;

import com.tst.models.entities.ProjectDistrict;
import com.tst.repositories.ProjectDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectDistrictService implements IProjectDistrictService {

    private final ProjectDistrictRepository projectDistrictRepository;


    @Override
    public Optional<ProjectDistrict> findById(Long id) {
        return projectDistrictRepository.findById(id);
    }

    @Override
    public void delete(ProjectDistrict projectDistrict) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
